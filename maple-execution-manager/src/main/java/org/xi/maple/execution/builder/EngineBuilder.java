package org.xi.maple.execution.builder;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.xi.maple.builder.convertor.MapleConvertor;
import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.builder.model.EngineExecutionModel;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.util.ActionUtils;
import org.xi.maple.common.util.RetryUtils;
import org.xi.maple.execution.client.PersistenceClient;
import org.xi.maple.execution.configuration.EngineManagerProperties;
import org.xi.maple.execution.builder.spi.EnginePluginService;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Component
public class EngineBuilder {

    private static final Logger logger = LoggerFactory.getLogger(EngineBuilder.class);

    final EnginePluginService enginePluginService;
    final EngineManagerProperties engineManagerProperties;
    final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    final PersistenceClient persistenceClient;

    public EngineBuilder(EnginePluginService enginePluginService, EngineManagerProperties pluginProperties, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient) {
        this.enginePluginService = enginePluginService;
        this.engineManagerProperties = pluginProperties;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.persistenceClient = persistenceClient;
    }

    public void execute(EngineExecutionDetailResponse execution) {
        updateExecutionStatus(execution.getId(), EngineExecutionStatus.STARTING);
        MapleConvertor convertor = enginePluginService.getConvertor(execution.getEngineCategory(), execution.getEngineVersion());
        List<CommandGeneratorModel> commandGenerators = convertor.getCommandGenerator(convert(execution));
        String startFile = null;
        String execHome = getPath(engineManagerProperties.getExecHome(), execution.getEngineCategory(), execution.getEngineVersion(), String.valueOf(execution.getId()));
        for (CommandGeneratorModel generatorModel : commandGenerators) {
            String ftlPath = generatorModel.getFtlPath();
            String fileName = generatorModel.getFilePath();
            ActionUtils.executeQuietly(() -> generateFile(execHome, ftlPath, fileName, generatorModel.getRequestModel()));
            if (generatorModel.isStartCommand()) {
                startFile = fileName;
            }
        }
        ProcessBuilder processBuilder = new ProcessBuilder("sh", getPath(engineManagerProperties.getExecHome(), startFile));

        threadPoolTaskExecutor.submit(() -> {
            Process process = null;
            try {
                process = processBuilder.start();
                int exitcode = process.waitFor();
                if (exitcode != 0) {
                    updateExecutionStatus(execution.getId(), EngineExecutionStatus.START_FAILED);
                }
            } catch (Throwable t) {
                logger.error("Execution[" + execution.getId() + "] starts failed!", t);
                updateExecutionStatus(execution.getId(), EngineExecutionStatus.START_FAILED);
            } finally {
                if (process != null && process.isAlive()) {
                    ActionUtils.executeQuietly(process::destroyForcibly);
                }
            }
        });
    }

    private Integer updateExecutionStatus(Integer id, String status) {
        Supplier<Integer> updateStatus = () -> persistenceClient.updateExecutionStatusById(new EngineExecutionUpdateStatusRequest(id, status));
        return RetryUtils.retry(updateStatus, 3, 1000, String.format("更新状态失败, id: %d, status: %s", id, status));
    }

    private long getPid(Process process) {
        long pid = -1;
        try {
            if (process.getClass().getName().equals("java.lang.UNIXProcess")) {
                java.lang.reflect.Field field = process.getClass().getDeclaredField("pid");
                field.setAccessible(true);
                pid = field.getLong(process);
                field.setAccessible(false);
            }
        } catch (Exception e) {
            pid = -1;
        }
        return pid;
    }

    private String getPath(String... more) {
        return String.join("/", more).replaceAll("/+", "/");
    }

    private EngineExecutionModel convert(EngineExecutionDetailResponse execution) {
        return EngineExecutionModel.builder()
                .execId(execution.getId())
                .uniqueId(execution.getUniqueId())
                .execName(execution.getExecName())
                .execComment(execution.getExecComment())
                .execContent(execution.getExecContent())
                .clusterQueue(execution.getClusterQueue())
                .group(execution.getGroup())
                .user(execution.getUser())
                .configuration(execution.getConfiguration())
                .webhooks(execution.getWebhooks())
                .build();
    }


    private void generateFile(String execHome, String ftlPath, String fileName, Object dataModel) throws IOException, TemplateException {
        String pluginHome = engineManagerProperties.getPluginHome();
        Path path = Paths.get(execHome, fileName);
        Path dir = path.getParent();
        if (Files.notExists(dir)) {
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxr-x");
            FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
            Files.createDirectories(dir, fileAttributes);
        }

        Configuration cfg = new Configuration(freemarker.template.Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(new File(pluginHome));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        try (FileWriter out = new FileWriter(path.toFile())) {
            Template ftl = cfg.getTemplate(ftlPath);
            ftl.process(dataModel, out);
            out.flush();
        }
    }
}
