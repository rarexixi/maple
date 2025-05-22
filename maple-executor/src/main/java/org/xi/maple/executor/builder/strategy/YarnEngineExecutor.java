package org.xi.maple.executor.builder.strategy;

import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.xi.maple.builder.convertor.MapleConvertor;
import org.xi.maple.builder.exception.GenerateCommandsException;
import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.builder.model.EngineExecutionModel;
import org.xi.maple.builder.model.ExecInfoPattern;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.executor.builder.BaseEngineExecutor;
import org.xi.maple.executor.builder.EngineExecutor;
import org.xi.maple.executor.builder.spi.EnginePluginService;
import org.xi.maple.executor.client.PersistenceClient;
import org.xi.maple.executor.configuration.ExecutionProperties;
import org.xi.maple.executor.configuration.PluginProperties;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class YarnEngineExecutor extends BaseEngineExecutor implements EngineExecutor {

    private static final Logger logger = LoggerFactory.getLogger(YarnEngineExecutor.class);

    public YarnEngineExecutor(EnginePluginService enginePluginService, ExecutionProperties executionProperties, PluginProperties pluginProperties, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient) {
        super(enginePluginService, executionProperties, pluginProperties, threadPoolTaskExecutor, persistenceClient);
    }

    public void execute(EngineExecutionModel execution) {
        updateExecutionStatus(execution.getExecId(), EngineExecutionStatus.STARTING);

        MapleConvertor convertor = enginePluginService.getConvertor(execution.getClusterCategory(), execution.getEngineCategory(), execution.getEngineVersion(), () -> {
            logger.error("Execution[{}] starts failed! ", execution.getExecId());
            updateExecutionStatus(execution.getExecId(), EngineExecutionStatus.START_FAILED);
        });

        threadPoolTaskExecutor.submit(() -> {
            List<CommandGeneratorModel> commandGenerators = convertor.getSubmitCommandGenerator(execution);
            CommandModel commandModel = generateCommands(commandGenerators, execution, () -> updateExecutionStatus(execution.getExecId(), EngineExecutionStatus.FAILED));
            ProcessBuilder processBuilder = new ProcessBuilder(getPath(commandModel.getExecHome(), commandModel.getStartFile()));

            Path log = Paths.get(commandModel.getExecHome(), "startup.log");

            try (final Writer logWriter = new BufferedWriter(new FileWriter(log.toFile(), true))) {
                Process process = processBuilder.start();

                final Map<String, String> resultMap = new HashMap<>(convertor.getExecInfoPatterns().size());
                MutableObject<String> clusterAppId = new MutableObject<>();
                MutableObject<String> clusterAppAddress = new MutableObject<>();
                Consumer<String> lineConsumer = (line) -> {
                    findAndSet(line, convertor.getClusterAppIdPatterns(), clusterAppId);
                    findAndSet(line, convertor.getClusterAppWebUrl(), clusterAppAddress);
                    findAndSet(line, convertor.getExecInfoPatterns(), resultMap);
                };

                threadPoolTaskExecutor.submit(() -> writeLogFile(process.getInputStream(), logWriter, lineConsumer));
                threadPoolTaskExecutor.submit(() -> writeLogFile(process.getErrorStream(), logWriter, lineConsumer));
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    updateExecutionStatus(execution.getExecId(), EngineExecutionStatus.FAILED);
                }
                // 处理完毕后, 更新执行状态
                if (StringUtils.isNotBlank(clusterAppId.getValue()) || StringUtils.isNotBlank(clusterAppAddress.getValue())) {
                    setClusterInfo(execution.getExecId(), clusterAppId.getValue(), clusterAppAddress.getValue());
                }
                if (!resultMap.isEmpty()) {
                    updateExecutionExecInfo(execution.getExecId(), resultMap);
                }
            } catch (Throwable t) {
                logger.error("Execution[{}] starts failed!", execution.getExecId(), t);
                updateExecutionStatus(execution.getExecId(), EngineExecutionStatus.FAILED);
            }
        });
    }

    private void findAndSet(String line, Pattern pattern, MutableObject<String> target) {
        if (StringUtils.isBlank(target.getValue())) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                target.setValue(matcher.group());
            }
        }
    }

    private void findAndSet(String line, List<ExecInfoPattern> patterns, Map<String, String> target) {
        if (target.size() != patterns.size()) {
            for (ExecInfoPattern logPattern : patterns) {
                if (target.containsKey(logPattern.getKey())) {
                    continue;
                }
                Matcher matcher = logPattern.getPattern().matcher(line);
                if (matcher.find()) {
                    target.put(logPattern.getKey(), matcher.group());
                }
            }
        }
    }

    @Override
    public void operate(EngineExecutionModel execution) {
        MapleConvertor convertor = enginePluginService.getConvertor(execution.getClusterCategory(), execution.getEngineCategory(), execution.getEngineVersion());
        List<CommandGeneratorModel> commandGenerators = convertor.getOperateCommandGenerator(execution);
        CommandModel commandModel = generateCommands(commandGenerators, execution, null);

        ProcessBuilder processBuilder = new ProcessBuilder("sh", getPath(commandModel.getExecHome(), commandModel.getStartFile()));
        threadPoolTaskExecutor.submit(() -> {
            StringWriter out = new StringWriter();
            try (final Writer logWriter = new BufferedWriter(out)) {
                Process process = processBuilder.start();
                threadPoolTaskExecutor.submit(() -> writeLogFile(process.getInputStream(), logWriter, null));
                threadPoolTaskExecutor.submit(() -> writeLogFile(process.getErrorStream(), logWriter, null));
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    logger.error("Execution[{}] {} failed! out: {}", execution.getExecId(), execution.getAction(), out);
                }
            } catch (Throwable t) {
                logger.error("Execution[{}] {} failed!", execution.getExecId(), execution.getAction(), t);
            }
        });
    }

    private void writeLogFile(InputStream is, Writer writer, Consumer<String> lineConsumer) {

        try (InputStreamReader in = new InputStreamReader(is);
             BufferedReader reader = new BufferedReader(in)) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
                if (lineConsumer != null) {
                    lineConsumer.accept(line);
                }
            }
        } catch (IOException e) {
            logger.error("error", e);
            throw new RuntimeException(e);
        }
    }
    private CommandModel generateCommands(List<CommandGeneratorModel> commandGenerators, EngineExecutionModel execution, Runnable generateFailedCallback) {
        String startFile = null;
        String execHome = getPath(executionProperties.getExecHome(), execution.getEngineCategory(), execution.getEngineVersion(), String.valueOf(execution.getExecId()));
        try {
            for (CommandGeneratorModel generatorModel : commandGenerators) {
                String ftlPath = generatorModel.getFtlPath();
                String fileName = generatorModel.getFilePath();
                generateFile(execHome, ftlPath, fileName, generatorModel.getRequestModel());
                if (generatorModel.isStartCommand()) {
                    startFile = fileName;
                }
            }
        } catch (TemplateException | IOException e) {
            logger.error("Execution[{}] {} failed! Generate file failed!", execution.getExecId(), execution.getAction(), e);
            if (generateFailedCallback != null) {
                generateFailedCallback.run();
            }
            throw new GenerateCommandsException("Generate command files failed!", e);
        }
        return new CommandModel(startFile, execHome);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    static class CommandModel {
        String startFile;
        String execHome;
    }
}
