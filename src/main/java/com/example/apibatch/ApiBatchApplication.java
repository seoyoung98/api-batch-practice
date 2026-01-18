package com.example.apibatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiBatchApplication.class, args);
    }

}
