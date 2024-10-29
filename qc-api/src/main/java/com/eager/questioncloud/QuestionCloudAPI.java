package com.eager.questioncloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class QuestionCloudAPI {
    public static void main(String[] args) {
        SpringApplication.run(QuestionCloudAPI.class, args);
    }
}
