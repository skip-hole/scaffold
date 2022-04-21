package com.scaffold.statemachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class StatemachineApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatemachineApplication.class, args);
    }
}
