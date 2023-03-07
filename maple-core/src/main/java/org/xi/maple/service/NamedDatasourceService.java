package org.xi.maple.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.model.NamedDatasource;
import org.xi.maple.util.RsaUtil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class NamedDatasourceService {
    private static final Logger logger = LoggerFactory.getLogger(NamedDatasourceService.class);

    private static volatile NamedDatasourceService metadataService;

    private RsaUtil rsaUtil;
    private String url;
    private String username;
    private String password;
    private String driver;
    private String getDatabasesSql;

    private NamedDatasourceService() {
    }

    public static NamedDatasource getDatasource(String mappingName) {
        NamedDatasourceService metadataService = getMetadataService();
        return metadataService.getDatabaseInternal(mappingName);
    }

    public static Map<String, NamedDatasource> getDatasourceMap(Collection<String> mappingNames) {
        NamedDatasourceService metadataService = getMetadataService();
        return metadataService.getDatabasesInternal(mappingNames);
    }

    private static NamedDatasourceService getMetadataService() {
        if (metadataService == null) {
            synchronized (NamedDatasourceService.class) {
                if (metadataService == null) {
                    Properties properties = new Properties();
                    try (InputStream metadata = NamedDatasourceService.class.getClassLoader().getResourceAsStream("named_database.properties")) {
                        properties.load(metadata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    metadataService = new NamedDatasourceService();
                    metadataService.url = properties.getProperty("jdbc.url");
                    metadataService.username = properties.getProperty("jdbc.username");
                    metadataService.password = properties.getProperty("jdbc.password");
                    metadataService.driver = properties.getProperty("jdbc.driver");
                    metadataService.getDatabasesSql = properties.getProperty("get-database-sql");

                    String publicKey = properties.getProperty("crypto.rsa-public-key");
                    String privateKey = properties.getProperty("crypto.rsa-private-key");
                    metadataService.rsaUtil = new RsaUtil(publicKey, privateKey);
                }
            }
        }
        return metadataService;
    }

    private NamedDatasource getDatabaseInternal(String mappingName) {
        List<String> databases = new ArrayList<>(1);
        databases.add(mappingName);
        Map<String, NamedDatasource> databaseMap = getDatabasesInternal(databases);
        return databaseMap.getOrDefault(mappingName, null);
    }

    private Map<String, NamedDatasource> getDatabasesInternal(Collection<String> mappingNames) {
        Map<String, NamedDatasource> result = new HashMap<>();
        if (mappingNames == null || mappingNames.isEmpty()) return result;

        StringBuilder sb = new StringBuilder("?");
        int size = mappingNames.size();
        for (int i = 1; i < size; i++) {
            sb.append(", ?");
        }
        String sql = getDatabasesSql.replace("${names}", sb);

        try {
            Class.forName(driver);
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                int i = 1;
                for (String mappingName : mappingNames) {
                    ps.setString(i++, mappingName);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String name = rs.getString("name");
                        String type = rs.getString("datasource_type");
                        String configJson = rs.getString("datasource_config");
                        DataSourceContext dataSourceContext = new DataSourceContext(type, configJson);
                        result.put(name, dataSourceContext.getDataSource());
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException throwable) {
            logger.error("Get meta database failed", throwable);
        }
        return result;
    }

}
