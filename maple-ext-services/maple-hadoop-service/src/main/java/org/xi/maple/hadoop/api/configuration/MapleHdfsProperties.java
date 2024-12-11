package org.xi.maple.hadoop.api.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "maple.hdfs")
@Data
public class MapleHdfsProperties {
    /**
     * hdfs 访问用户
     */
    private String username;
    /**
     * hdfs 相关配置
     */
    private Map<String, String> conf = new HashMap<>();
}