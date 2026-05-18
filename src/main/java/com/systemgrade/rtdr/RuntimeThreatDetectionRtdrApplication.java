package com.systemgrade.rtdr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RuntimeThreatDetectionRtdrApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuntimeThreatDetectionRtdrApplication.class, args);
    }

}
