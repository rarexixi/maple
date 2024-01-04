package org.xi.maple.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xishihao
 */
@EnableCaching
@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class MapleManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapleManagerApplication.class);
    }
}
