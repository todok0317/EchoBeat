package com.echobeat.favorite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.echobeat.favorite", "com.echobeat.common"})
@EnableDiscoveryClient
@EnableFeignClients
public class FavoriteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FavoriteServiceApplication.class, args);
    }
}
