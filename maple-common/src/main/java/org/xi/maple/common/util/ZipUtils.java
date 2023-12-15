package org.xi.maple.common.util;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    public static void compressContent(String zipFilePath, Collection<Pair<String, String>> files, String folder, String zipFolder) throws IOException {

        // 如果目录不存在就创建目录
        String directory = zipFilePath.substring(0, zipFilePath.lastIndexOf("/"));
        File file = new File(directory);
        if (!file.exists()) file.mkdirs();

        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            for (Pair<String, String> item : files) {
                byte[] bytes = item.getValue().getBytes();
                ZipEntry zipEntry = new ZipEntry(item.getKey());
                zipOut.putNextEntry(zipEntry);
                zipOut.write(bytes, 0, bytes.length);
            }

            File sourceFile = new File(folder);
            compress(sourceFile, folder.endsWith("/") ? folder : folder + "/", zipFolder.endsWith("/") ? zipFolder : zipFolder + "/", zipOut);
        }
    }

    static void compress(File sourceFile, String basePath, String zipFolder, ZipOutputStream zipOut) throws IOException {

        if (!sourceFile.exists()) return;
        if (sourceFile.isFile()) {
            try (FileInputStream fis = new FileInputStream(sourceFile)) {
                String zipPath = sourceFile.getAbsolutePath().substring(basePath.length()).replace("\\", "/");
                ZipEntry zipEntry = new ZipEntry(zipFolder + zipPath);
                zipOut.putNextEntry(zipEntry);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
            }
            return;
        }
        if (sourceFile.isDirectory()) {
            for (File fileToZip : sourceFile.listFiles()) {
                compress(fileToZip, basePath, zipFolder, zipOut);
            }
        }
    }
}
