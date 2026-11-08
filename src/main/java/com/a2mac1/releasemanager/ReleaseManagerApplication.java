package com.a2mac1.releasemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ReleaseManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReleaseManagerApplication.class, args);
    }

}
