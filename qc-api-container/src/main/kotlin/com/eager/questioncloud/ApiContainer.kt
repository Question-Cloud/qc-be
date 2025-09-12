package com.eager.questioncloud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
@EnableRetry
class ApiContainer

fun main(args: Array<String>) {
    runApplication<ApiContainer>(*args)
}