package org.xi.maple.execution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MapleEngineManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapleEngineManagerApplication.class, args);
    }
}
