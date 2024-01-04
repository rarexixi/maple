package org.xi.maple.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MapleExecutorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapleExecutorApplication.class, args);
    }
}
