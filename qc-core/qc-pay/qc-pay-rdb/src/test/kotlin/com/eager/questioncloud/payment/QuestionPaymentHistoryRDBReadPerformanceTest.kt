package com.eager.questioncloud.payment

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import com.eager.questioncloud.payment.domain.QuestionPaymentHistoryOrder
import io.hypersistence.tsid.TSID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.time.measureTime

@SpringBootTest
@ActiveProfiles("test")
class QuestionPaymentHistoryRDBReadPerformanceTest(
    @Autowired val questionPaymentHistoryRDBRepository: QuestionPaymentHistoryRDBRepository,
) {
    @Test
    fun mock() {
        val questionPaymentHistoryOrders =
            listOf(
                QuestionPaymentHistoryOrder(
                    1L,
                    1L,
                    1000,
                    500,
                    "promoton",
                    500,
                    "title1",
                    "thumb1",
                    "creator1",
                    "subject1",
                    "main",
                    "sub"
                ),
                QuestionPaymentHistoryOrder(
                    2L,
                    2L,
                    1000,
                    500,
                    "promoton",
                    500,
                    "title1",
                    "thumb1",
                    "creator1",
                    "subject1",
                    "main",
                    "sub"
                ),
                QuestionPaymentHistoryOrder(
                    3L,
                    3L,
                    1000,
                    500,
                    "promoton",
                    500,
                    "title1",
                    "thumb1",
                    "creator1",
                    "subject1",
                    "main",
                    "sub"
                ),
            )
        
        for (i in 1..100) {
            questionPaymentHistoryRDBRepository.save(
                QuestionPaymentHistory.create(TSID.Factory.getTsid().toString(), 1L, questionPaymentHistoryOrders, listOf(), 3000, 1500)
            )
        }
    }
    
    @Test
    fun read() {
        val cnt = 100000
        val executors = Executors.newFixedThreadPool(100)
        val countDownLatch = CountDownLatch(cnt)
        
        val time = measureTime {
            for (i in 1..cnt) {
                executors.submit {
                    try {
                        questionPaymentHistoryRDBRepository.getQuestionPaymentHistory(1L, PagingInformation(0, 100))
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
}