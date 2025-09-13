package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pg.PGConfirmRequest
import com.eager.questioncloud.common.pg.PaymentAPI
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
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
    @Autowired private val chargePointPaymentRepository: ChargePointPaymentRepository,
    @Autowired private val dbCleaner: DBCleaner,
) {
    @MockBean
    private lateinit var paymentAPI: PaymentAPI
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
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
    
    @Test
    fun `PG API 호출 시 400번대 응답이면 fail처리 된다`() {
        // given
        val userId = 1L
        val chargePointPaymenType = ChargePointType.PackageA
        val savedChargePointPayment = chargePointPaymentRepository.save(ChargePointPayment.createOrder(userId, chargePointPaymenType))
        
        whenever(paymentAPI.confirm(any())).thenThrow(CoreException::class.java)
        
        // then
        Assertions.assertThatThrownBy {
            chargePointPaymentPGProcessor.confirm(PGConfirmRequest("paymentId", savedChargePointPayment.orderId, 10000))
        }.isInstanceOf(CoreException::class.java)
        
        val chargePointPayment = chargePointPaymentRepository.findByOrderId(savedChargePointPayment.orderId)
        Assertions.assertThat(chargePointPayment.chargePointPaymentStatus).isEqualTo(ChargePointPaymentStatus.FAILED)
    }
}