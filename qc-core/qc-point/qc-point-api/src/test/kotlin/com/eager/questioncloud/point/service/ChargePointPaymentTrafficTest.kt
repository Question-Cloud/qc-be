package com.eager.questioncloud.point.service

import com.eager.questioncloud.common.pg.PGConfirmResponse
import com.eager.questioncloud.common.pg.PGPayment
import com.eager.questioncloud.common.pg.PGPaymentStatus
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.implement.ChargePointPaymentPGProcessor
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SpringBootTest
@ActiveProfiles("test")
class ChargePointPaymentTrafficTest(
    @Autowired private val chargePointPaymentRepository: ChargePointPaymentRepository,
    @Autowired private val chargePointPaymentService: ChargePointPaymentService,
) {
    @MockBean
    lateinit var chargePointPaymentPGProcessor: ChargePointPaymentPGProcessor
    
    @Test
    fun `포인트 충전 부하 테스트`() {
        // given
        val cppList = mutableListOf<ChargePointPayment>()
        for (i in 1..300) {
            cppList.add(
                chargePointPaymentRepository.save(
                    ChargePointPayment(
                        paymentId = UUID.randomUUID().toString(),
                        userId = 1L,
                        chargePointType = ChargePointType.PackageB,
                        chargePointPaymentStatus = ChargePointPaymentStatus.ORDERED,
                        requestAt = LocalDateTime.now()
                    )
                )
            )
        }
        
        whenever(chargePointPaymentPGProcessor.getPayment(any())).thenAnswer { e ->
            val orderId = e.getArgument<String>(0)
            PGPayment(UUID.randomUUID().toString(), orderId, ChargePointType.PackageB.amount, PGPaymentStatus.READY)
        }
        
        whenever(chargePointPaymentPGProcessor.confirm(any())).thenReturn(PGConfirmResponse(PGPaymentStatus.DONE))
        
        // when
        val countDownLatch = CountDownLatch(cppList.size)
        val executorService: ExecutorService = Executors.newFixedThreadPool(300)
        cppList.forEach { cpp ->
            executorService.submit {
                try {
                    chargePointPaymentService.approvePayment(cpp.orderId)
                } catch (e: Exception) {
                    println(e.message)
                } finally {
                    countDownLatch.countDown()
                }
            }
        }
        
        countDownLatch.await()
        executorService.shutdown()
    }
}