package org.xi.maple.executor.builder;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.executor.builder.spi.EnginePluginService;
import org.xi.maple.executor.client.PersistenceClient;
import org.xi.maple.executor.configuration.ExecutionProperties;
import org.xi.maple.executor.configuration.PluginProperties;
import org.xi.maple.persistence.model.request.EngineExecutionPatchReq;
import org.xi.maple.persistence.model.request.EngineExecutionStatusUpdateReq;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Map;
import java.util.Set;

public abstract class BaseEngineExecutor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final EnginePluginService enginePluginService;
    protected final ExecutionProperties executionProperties;
    protected final PluginProperties pluginProperties;
    protected final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    protected final PersistenceClient persistenceClient;

    public BaseEngineExecutor(EnginePluginService enginePluginService, ExecutionProperties executionProperties, PluginProperties pluginProperties, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient) {
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
        return persistenceClient.updateExecutionStatusById(id, new EngineExecutionStatusUpdateReq(status.toString()));
    }

    protected Integer setClusterInfo(Integer id, String clusterAppId, String clusterAppAddress) {
        EngineExecutionPatchReq req = new EngineExecutionPatchReq();
        req.setClusterAppId(clusterAppId);
        req.setClusterAppAddress(clusterAppAddress);
        return persistenceClient.patchExecutionById(id, req);
    }

    protected Integer updateExecutionExecInfo(Integer id, Map<String, ?> execInfo) {
        return persistenceClient.updateExecutionExecInfoById(id, execInfo);
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

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
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

        File file = path.toFile();
        try (FileWriter out = new FileWriter(file)) {
            String content = generateFileContent(execHome, ftlPath, fileName, dataModel);
            out.write(content);
            out.flush();
        }
        file.setExecutable(true);
    }
}
