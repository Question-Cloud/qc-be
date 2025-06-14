package com.eager.questioncloud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiContainer

fun main(args: Array<String>) {
    runApplication<ApiContainer>(*args)
}