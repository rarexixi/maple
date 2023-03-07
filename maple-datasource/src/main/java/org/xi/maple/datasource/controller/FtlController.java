package org.xi.maple.datasource.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.Map;

@CrossOrigin
@RequestMapping("/ftl")
@RestController
@Validated
public class FtlController {

    @Value("${freemarker.template.path}")
    private String templatePath;

    @PostMapping("/get-code")
    public ResponseEntity<String> getCode(@RequestBody Map<String, Object> dataModel) throws IOException, TemplateException {
        Configuration cfg = new Configuration(freemarker.template.Configuration.VERSION_2_3_32);
        cfg.setDirectoryForTemplateLoading(new File(templatePath));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Template ftl = cfg.getTemplate("maple.ftl");
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(65535);
             OutputStreamWriter out = new OutputStreamWriter(outputStream)) {
            ftl.process(dataModel, out);
            String result = outputStream.toString("UTF-8");
            return ResponseEntity.created(URI.create("")).body(result);
        }
    }
}
