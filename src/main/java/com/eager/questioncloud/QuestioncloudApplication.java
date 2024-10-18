package com.eager.questioncloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class QuestioncloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuestioncloudApplication.class, args);
    }

}
