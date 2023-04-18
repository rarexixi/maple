package org.xi.maple.common.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class FreemarkerUtils {

    private static final Logger logger = LoggerFactory.getLogger(FreemarkerUtils.class);

    static final Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);

    /**
     * 执行生成，异常时返回空字符串
     *
     * @param template 模版
     * @param model    对象
     * @return 结果
     */
    public static String process(String template, Object model) {

        if (StringUtils.isBlank(template)) {
            return "";
        }

        try {
            return originalProcess(template, model);
        } catch (IOException | TemplateException e) {
            logger.error("模版生成异常：", e);
        }
        return "";
    }

    /**
     * 执行生成，异常时返回空字符串
     *
     * @param template 模版
     * @param model    对象
     * @return 结果
     */
    public static String process(Configuration configuration, String template, Object model) {

        if (StringUtils.isBlank(template)) {
            return "";
        }

        try {
            return originalProcess(configuration, template, model);
        } catch (IOException | TemplateException e) {
            logger.error("模版生成异常：", e);
        }
        return "";
    }

    /**
     * 执行生成，有异常时抛出
     *
     * @param template 模版
     * @param model    对象
     * @return 结果
     * @throws IOException
     * @throws TemplateException
     */
    public static String originalProcess(String template, Object model) throws IOException, TemplateException {

        if (StringUtils.isBlank(template)) {
            return "";
        }

        StringWriter out = new StringWriter();
        new Template("template", new StringReader(template), configuration).process(model, out);
        return out.toString();
    }

    /**
     * 执行生成，有异常时抛出
     *
     * @param template 模版
     * @param model    对象
     * @return 结果
     * @throws IOException
     * @throws TemplateException
     */
    public static String originalProcess(Configuration configuration, String template, Object model) throws IOException, TemplateException {

        if (StringUtils.isBlank(template)) {
            return "";
        }

        StringWriter out = new StringWriter();
        new Template("template", new StringReader(template), configuration).process(model, out);
        return out.toString();
    }
}
