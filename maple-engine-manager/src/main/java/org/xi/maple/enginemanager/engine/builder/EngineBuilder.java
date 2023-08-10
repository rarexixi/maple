package org.xi.maple.enginemanager.engine.builder;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.stereotype.Component;
import org.xi.maple.builder.convertor.MapleConvertor;
import org.xi.maple.builder.model.CommandGeneratorModel;
import org.xi.maple.common.util.ActionUtils;
import org.xi.maple.enginemanager.configuration.EngineProperties;
import org.xi.maple.enginemanager.configuration.PluginProperties;
import org.xi.maple.enginemanager.spi.EnginePluginService;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;

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

    public void execute(EngineExecutionAddRequest execution) throws IOException {
        MapleConvertor convertor = enginePluginService.getConvertor(execution.getEngineCategory(), execution.getEngineVersion());
        List<CommandGeneratorModel> commandGenerators = convertor.getCommandGenerator();
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
        processBuilder.start();
    }

    String getPath(String... more) {
        return String.join("/", more).replaceAll("/+", "/");
    }


    private void generateFile(String ftlPath, String fileName, Object dataModel) throws IOException, TemplateException {
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
