package org.xi.maple.execution.builder;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.xi.maple.builder.model.EngineExecutionModel;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.model.EngineConf;
import org.xi.maple.execution.client.PersistenceClient;
import org.xi.maple.execution.configuration.ExecutionProperties;
import org.xi.maple.execution.builder.spi.EnginePluginService;
import org.xi.maple.execution.configuration.PluginProperties;
import org.xi.maple.execution.service.EngineExecutionService;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfGetRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public abstract class EngineExecutor implements EngineExecutionService {

    private final Logger logger;

    protected final EnginePluginService enginePluginService;
    protected final ExecutionProperties executionProperties;
    protected final PluginProperties pluginProperties;
    protected final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    protected final PersistenceClient persistenceClient;

    public EngineExecutor(Logger logger, EnginePluginService enginePluginService, ExecutionProperties executionProperties, PluginProperties pluginProperties, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient) {
        this.logger = logger;
        this.enginePluginService = enginePluginService;
        this.executionProperties = executionProperties;
        this.pluginProperties = pluginProperties;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.persistenceClient = persistenceClient;
    }

    /**
     * 修改执行状态，状态变更逻辑已在接口实现
     *
     * @param id     执行ID
     * @param status 变更状态
     * @return 修改的数据量
     */
    protected Integer updateExecutionStatus(Integer id, EngineExecutionStatus status) {
        return persistenceClient.updateExecutionStatusById(id, new EngineExecutionUpdateStatusRequest(status.toString())); // todo
    }

    protected long getPid(Process process) {
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

    protected String getPath(String... more) {
        return String.join("/", more).replaceAll("/+", "/");
    }

    protected EngineExecutionModel convert(EngineExecutionDetailResponse execution) {
        ClusterEngineDefaultConfGetRequest request = new ClusterEngineDefaultConfGetRequest(execution.getCluster(), execution.getEngineCategory(), execution.getEngineVersion(), execution.getGroup(), execution.getUser());
        EngineConf engineConf = persistenceClient.getEngineConf(request);
        return new EngineExecutionModel().withExecId(execution.getId())
                .withExecFile(execution.getExecFile())
                .withFromApp(execution.getFromApp())
                .withJobId(execution.getJobId())
                .withBizId(execution.getBizId())
                .withExecUniqId(execution.getExecUniqId())
                .withExecName(execution.getExecName())
                .withResourceGroup(execution.getResourceGroup())
                .withGroup(execution.getGroup())
                .withUser(execution.getUser())
                .withEngine(engineConf)
                .withConfiguration(execution.getConfiguration());
    }

    /**
     * 生成最终的可执行文件
     *
     * @param execHome  生成目录地址
     * @param ftlPath   模板路径
     * @param fileName  生成的文件
     * @param dataModel 模板数据模型
     * @throws IOException
     * @throws TemplateException
     */
    protected String generateFileContent(String execHome, String ftlPath, String fileName, Object dataModel) throws IOException, TemplateException {
        String pluginHome = pluginProperties.getFtlPath();

        Configuration cfg = new Configuration(freemarker.template.Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(new File(pluginHome));
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        try (StringWriter sw = new StringWriter(4096)) {
            Template ftl = cfg.getTemplate(ftlPath);
            ftl.process(dataModel, sw);
            sw.flush();
            return sw.toString();
        }
    }

    /**
     * 生成最终的可执行文件
     *
     * @param execHome  生成目录地址
     * @param ftlPath   模板路径
     * @param fileName  生成的文件
     * @param dataModel 模板数据模型
     * @throws IOException
     * @throws TemplateException
     */
    protected void generateFile(String execHome, String ftlPath, String fileName, Object dataModel) throws IOException, TemplateException {
        Path path = Paths.get(execHome, fileName);
        Path dir = path.getParent();
        if (Files.notExists(dir)) {
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxr-x");
            FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
            Files.createDirectories(dir, fileAttributes);
        }

        try (FileWriter out = new FileWriter(path.toFile())) {
            String content = generateFileContent(execHome, ftlPath, fileName, dataModel);
            out.write(content);
            out.flush();
        }
    }
}
