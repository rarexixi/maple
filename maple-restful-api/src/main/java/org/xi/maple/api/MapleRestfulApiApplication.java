package org.xi.maple.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xishihao
 */
@SpringBootApplication//(scanBasePackages = {"org.xi.maple.persistence.mapper", "org.xi.maple.api"})
public class MapleRestfulApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapleRestfulApiApplication.class, args);
    }
}
