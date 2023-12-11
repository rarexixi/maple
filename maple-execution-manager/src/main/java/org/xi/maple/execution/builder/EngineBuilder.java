package org.xi.maple.execution.builder;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.xi.maple.builder.model.EngineExecutionModel;
import org.xi.maple.common.util.RetryUtils;
import org.xi.maple.execution.client.PersistenceClient;
import org.xi.maple.execution.configuration.ExecutionProperties;
import org.xi.maple.execution.builder.spi.EnginePluginService;
import org.xi.maple.execution.configuration.PluginProperties;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import java.util.function.Supplier;

public abstract class EngineBuilder<T> {

    private final Logger logger;

    final EnginePluginService enginePluginService;
    final ExecutionProperties executionProperties;
    final PluginProperties pluginProperties;
    final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    final PersistenceClient persistenceClient;

    public EngineBuilder(EnginePluginService enginePluginService, ExecutionProperties executionProperties, PluginProperties pluginProperties, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient) {
        this.enginePluginService = enginePluginService;
        this.executionProperties = executionProperties;
        this.pluginProperties = pluginProperties;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.persistenceClient = persistenceClient;
        logger = getLogger();
    }

    public abstract T execute(EngineExecutionDetailResponse execution);
    public abstract Logger getLogger();

    /**
     * 修改执行状态，状态变更逻辑已在接口实现
     *
     * @param id     执行ID
     * @param status 变更状态
     * @return 修改的数据量
     */
    protected Integer updateExecutionStatus(Integer id, String status) {
        Supplier<Integer> updateStatus = () -> persistenceClient.updateExecutionStatusById(new EngineExecutionUpdateStatusRequest(id, status));
        return RetryUtils.retry(updateStatus, 3, 1000, String.format("更新状态失败, id: %d, status: %s", id, status));
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
        cfg.setDefaultEncoding("UTF-8");
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
