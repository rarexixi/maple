package org.xi.maple.mp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCaching
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class MapleManagementPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(MapleManagementPlatformApplication.class, args);
	}
}


