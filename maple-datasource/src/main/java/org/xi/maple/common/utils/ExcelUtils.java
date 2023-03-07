package org.xi.maple.common.utils;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class ExcelUtils {

    public static <T> void export(HttpServletResponse response, List<T> data, Class<T> clazz, String exportName, String defaultName) throws IOException {
        String fileName = StringUtils.isBlank(exportName) ? defaultName : exportName;
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", "utf-8"));
        EasyExcel.write(response.getOutputStream(), clazz).sheet().doWrite(data);
    }
}
