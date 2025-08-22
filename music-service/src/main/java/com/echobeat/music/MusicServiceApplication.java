package com.echobeat.music;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.echobeat.music", "com.echobeat.common"})
@EnableDiscoveryClient
@EnableScheduling
public class MusicServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicServiceApplication.class, args);
    }
}
