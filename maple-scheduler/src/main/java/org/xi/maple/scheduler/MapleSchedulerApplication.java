package org.xi.maple.scheduler;

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
public class MapleSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapleSchedulerApplication.class);
    }
}
