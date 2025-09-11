package com.eager.questioncloud.point.scheduler

import com.eager.questioncloud.common.pg.PGConfirmResponse
import com.eager.questioncloud.common.pg.PGPaymentStatus
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.implement.ChargePointPaymentPGProcessor
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class ChargePointPaymentCheckSchedulerTest(
    @Autowired private val chargePointPaymentCheckScheduler: ChargePointPaymentCheckScheduler,
    @Autowired private val chargePointPaymentRepository: ChargePointPaymentRepository,
    
    ) {
    @MockBean
    lateinit var chargePointPaymentPGProcessor: ChargePointPaymentPGProcessor;
    
    @Test
    fun `포인트 충전 결제 복구 스케줄러 테스트`() {
        // given
        for (i in 1..10) {
            chargePointPaymentRepository.save(
                ChargePointPayment(
                    paymentId = UUID.randomUUID().toString(),
                    userId = 1L,
                    chargePointType = ChargePointType.PackageB,
                    chargePointPaymentStatus = ChargePointPaymentStatus.PENDING_PG_PAYMENT,
                    requestAt = LocalDateTime.now().minusMinutes(10)
                )
            )
        }
        
        whenever(chargePointPaymentPGProcessor.confirm(any())).thenReturn(PGConfirmResponse(PGPaymentStatus.DONE))
        
        // when
        chargePointPaymentCheckScheduler.task()
        
        // then
        val result = chargePointPaymentRepository.getPendingPayments()
        Assertions.assertThat(result.size).isEqualTo(0)
    }
}