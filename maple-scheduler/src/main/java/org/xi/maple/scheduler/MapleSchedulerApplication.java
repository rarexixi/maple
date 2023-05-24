package org.xi.maple.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xishihao
 */
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class MapleSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapleSchedulerApplication.class);
    }
}
