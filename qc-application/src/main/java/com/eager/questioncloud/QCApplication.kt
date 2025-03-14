package com.eager.questioncloud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class QCApplication

fun main(args: Array<String>) {
    runApplication<QCApplication>(*args)
}
