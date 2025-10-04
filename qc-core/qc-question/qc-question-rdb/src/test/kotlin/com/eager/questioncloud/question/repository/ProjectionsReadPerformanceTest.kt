package com.eager.questioncloud.question.repository

import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.time.measureTime

@SpringBootTest
@ActiveProfiles("local")
@ApplyExtension(SpringExtension::class)
class ProjectionsReadPerformanceTest(
    private val projectionsReadPerformanceMock: ProjectionsReadPerformanceMock
) : FunSpec({
    test("simple Select Test") {
        val cnt = 500000
        val executors = Executors.newFixedThreadPool(100)
        val countDownLatch = CountDownLatch(cnt)
        
        val time = measureTime {
            for (i in 1..cnt) {
                executors.submit {
                    try {
                        projectionsReadPerformanceMock.simpleSelect()
                    } finally {
                        countDownLatch.countDown()
                    }
                }
            }
            countDownLatch.await()
        }
        
        println(time)
        
        executors.shutdown()
        projectionsReadPerformanceMock.simpleSelect()
    }
    
    test("prjections Select Test") {
        val cnt = 500000
        val executors = Executors.newFixedThreadPool(100)
        val countDownLatch = CountDownLatch(cnt)
        
        val time = measureTime {
            for (i in 1..cnt) {
                executors.submit {
                    try {
                        projectionsReadPerformanceMock.projectionsSelect()
                    } finally {
                        countDownLatch.countDown()
                    }
                }
            }
            countDownLatch.await()
        }
        
        println(time)
        
        executors.shutdown()
    }
    
    test("jpa Select Test") {
        val cnt = 500000
        val executors = Executors.newFixedThreadPool(100)
        val countDownLatch = CountDownLatch(cnt)
        
        val time = measureTime {
            for (i in 1..cnt) {
                executors.submit {
                    try {
                        projectionsReadPerformanceMock.jpaSelect()
                    } finally {
                        countDownLatch.countDown()
                    }
                }
            }
            countDownLatch.await()
        }
        
        println(time)
        
        executors.shutdown()
    }
})
