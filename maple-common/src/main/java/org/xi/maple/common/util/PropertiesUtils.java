package org.xi.maple.common.util;

import org.xi.maple.common.exception.MapleException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesUtils {

    public static Properties getProperties(String url) throws FileNotFoundException {

        Path path = Paths.get(url);

        if (!Files.exists(path)) {
            throw new FileNotFoundException(path + " not exists");
        }
        try (InputStream inputStream = Files.newInputStream(path)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new MapleException(e.getMessage());
        }
    }
}
