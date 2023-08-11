package org.xi.maple.enginemanager.engine.builder;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.stereotype.Component;
import org.xi.maple.builder.convertor.MapleConvertor;
import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.builder.model.EngineExecutionModel;
import org.xi.maple.common.util.ActionUtils;
import org.xi.maple.enginemanager.configuration.EngineProperties;
import org.xi.maple.enginemanager.configuration.PluginProperties;
import org.xi.maple.enginemanager.spi.EnginePluginService;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

import java.io.*;
import java.util.List;

@Component
public class EngineBuilder {

    final EnginePluginService enginePluginService;
    final EngineProperties engineProperties;
    final PluginProperties pluginProperties;

    public EngineBuilder(EnginePluginService enginePluginService, EngineProperties engineProperties, PluginProperties pluginProperties) {
        this.enginePluginService = enginePluginService;
        this.engineProperties = engineProperties;
        this.pluginProperties = pluginProperties;
    }

    public Long execute(EngineExecutionDetailResponse execution) throws IOException {
        MapleConvertor convertor = enginePluginService.getConvertor(execution.getEngineCategory(), execution.getEngineVersion());
        List<CommandGeneratorModel> commandGenerators = convertor.getCommandGenerator(convert(execution));
        String startFile = null;
        for (CommandGeneratorModel generatorModel : commandGenerators) {
            String ftlPath = generatorModel.getFtlPath();
            String fileName = generatorModel.getFilePath();
            ActionUtils.executeQuietly(() -> generateFile(ftlPath, fileName, generatorModel.getRequestModel()));
            if (generatorModel.isStartCommand()) {
                startFile = fileName;
            }
        }
        ProcessBuilder processBuilder = new ProcessBuilder("sh", getPath(engineProperties.getExecHome(), startFile));
        Process process = processBuilder.start();
        // 获取process pid
        long pid = getPid(process);
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
        Configuration cfg = new Configuration(freemarker.template.Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(new File(pluginProperties.getHome()));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        try (FileWriter out = new FileWriter(fileName)) {
            Template ftl = cfg.getTemplate(ftlPath);
            ftl.process(dataModel, out);
            out.flush();
        }
    }
}
