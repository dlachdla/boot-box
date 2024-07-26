package com.sh.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BootboxApplication {

    public static void main(String[] args) {

        SpringApplication.run(BootboxApplication.class, args);
        System.out.println("========start!========");

    }

}
