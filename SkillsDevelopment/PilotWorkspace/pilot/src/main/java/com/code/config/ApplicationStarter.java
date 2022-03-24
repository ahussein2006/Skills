package com.code.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.code")
public class ApplicationStarter {

    public static void main(String[] args) {
	SpringApplication.run(ApplicationStarter.class, args);
    }

}
