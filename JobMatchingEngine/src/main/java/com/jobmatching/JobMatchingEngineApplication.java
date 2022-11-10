package com.jobmatching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@PropertySource(value = { "classpath:custom.properties",
        "classpath:custom-${spring.profiles.active:}.properties" }, ignoreResourceNotFound = true)
@Slf4j
public class JobMatchingEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobMatchingEngineApplication.class, args);
        log.info("~~~~~~~~~~FINDING JOBS~~~~~~~~~~");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
