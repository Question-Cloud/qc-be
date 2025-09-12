package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pg.PGConfirmRequest
import com.eager.questioncloud.common.pg.PaymentAPI
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class ChargePointPaymentPGProcessorTest(
    @Autowired private val chargePointPaymentPGProcessor: ChargePointPaymentPGProcessor,
) {
    @MockBean
    private lateinit var paymentAPI: PaymentAPI
    
    @Test
    fun `Recover 동작 테스트`() {
        // given
        whenever(paymentAPI.confirm(any())).thenThrow(RuntimeException::class.java)
        
        // when then
        Assertions.assertThatThrownBy {
            chargePointPaymentPGProcessor.confirm(PGConfirmRequest("paymentId", "orderId", 10000))
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.PAYMENT_ERROR)
    }
}