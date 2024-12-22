package org.xi.maple.jdbc.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author xishihao
 */
@EnableEurekaClient
@SpringBootApplication
public class MapleJdbcServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapleJdbcServiceApplication.class, args);
    }
}
