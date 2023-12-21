package org.xi.maple.datacalc.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author xishihao
 */
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class MapleRestfulApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapleRestfulApiApplication.class, args);
    }
}
