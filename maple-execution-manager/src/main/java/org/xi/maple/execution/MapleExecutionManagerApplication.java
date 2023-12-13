package org.xi.maple.execution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MapleExecutionManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapleExecutionManagerApplication.class, args);
    }
}
