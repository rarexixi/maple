package org.xi.maple.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author xishihao
 */
@EnableCaching
@EnableFeignClients
@SpringBootApplication
public class MapleRestfulApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapleRestfulApiApplication.class, args);
    }
}
