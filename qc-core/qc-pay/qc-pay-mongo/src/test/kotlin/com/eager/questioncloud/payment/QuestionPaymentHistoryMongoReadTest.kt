package com.eager.questioncloud.payment

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import com.eager.questioncloud.payment.domain.QuestionPaymentHistoryOrder
import com.eager.questioncloud.payment.repository.QuestionPaymentHistoryRepository
import io.hypersistence.tsid.TSID
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.time.measureTime

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class QuestionPaymentHistoryMongoReadTest(
    private val questionPaymentHistoryRepository: QuestionPaymentHistoryRepository,
) : FunSpec() {
    
    init {
        test("mock data 생성") {
            val questionPaymentHistoryOrders =
                listOf(
                    QuestionPaymentHistoryOrder(
                        1L,
                        1L,
                        1000,
                        500,
                        "promotionTitle",
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
                        "promotionTitle",
                        500,
                        "title2",
                        "thumb2",
                        "creator2",
                        "subject1",
                        "main",
                        "sub"
                    ),
                    QuestionPaymentHistoryOrder(
                        3L,
                        3L,
                        1000,
                        500,
                        "promotionTitle",
                        500,
                        "title3",
                        "thumb3",
                        "creator3",
                        "subject1",
                        "main",
                        "sub"
                    ),
                )
            
            for (i in 1..100) {
                questionPaymentHistoryRepository.save(
                    QuestionPaymentHistory.create(
                        TSID.Factory.getTsid().toString(),
                        1L,
                        questionPaymentHistoryOrders,
                        emptyList(),
                        3000,
                        1500
                    )
                )
            }
        }
        
        test("read 성능 테스트") {
            val cnt = 100000
            val executors = Executors.newFixedThreadPool(100)
            val countDownLatch = CountDownLatch(cnt)
            
            val time = measureTime {
                for (i in 1..cnt) {
                    executors.submit {
                        try {
                            questionPaymentHistoryRepository.getQuestionPaymentHistory(1L, PagingInformation(0, 100))
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
}