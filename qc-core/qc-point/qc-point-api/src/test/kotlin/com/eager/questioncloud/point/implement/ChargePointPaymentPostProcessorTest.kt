package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.pg.PGConfirmResult
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.domain.UserPoint
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import com.eager.questioncloud.point.repository.UserPointRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class ChargePointPaymentPostProcessorTest(
    @Autowired val chargePointPaymentRepository: ChargePointPaymentRepository,
    @Autowired val chargePointPaymentPostProcessor: ChargePointPaymentPostProcessor,
    @Autowired val userPointRepository: UserPointRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `포인트 충전을 처리할 수 있다`() {
        // given
        val userId = 1L
        val orderId = UUID.randomUUID().toString()
        val paymentId = UUID.randomUUID().toString()
        val chargePointType = ChargePointType.PackageA
        userPointRepository.save(UserPoint.create(userId))
        
        val chargePointPayment = ChargePointPayment(
            orderId = orderId,
            paymentId = paymentId,
            userId = userId,
            chargePointType = chargePointType,
            ChargePointPaymentStatus.PENDING_PG_PAYMENT
        )
        
        chargePointPaymentRepository.save(chargePointPayment)
        
        val pgConfirmResult = PGConfirmResult.Success(
            orderId,
            paymentId
        )
        
        // when
        chargePointPaymentPostProcessor.postProcess(chargePointPayment, pgConfirmResult)
        
        //then
        val userPoint = userPointRepository.getUserPoint(userId)
        Assertions.assertThat(userPoint.point).isEqualTo(chargePointType.amount)
    }
    
    @Test
    fun `결제 승인에 실패했을 경우 Fail 처리 된다`() {
        // given
        val userId = 1L
        val orderId = UUID.randomUUID().toString()
        val paymentId = UUID.randomUUID().toString()
        val chargePointType = ChargePointType.PackageA
        userPointRepository.save(UserPoint.create(userId))
        
        val savedChargePointPayment = ChargePointPayment(
            orderId = orderId,
            paymentId = paymentId,
            userId = userId,
            chargePointType = chargePointType,
            ChargePointPaymentStatus.PENDING_PG_PAYMENT
        )
        
        chargePointPaymentRepository.save(savedChargePointPayment)
        
        val pgConfirmResult = PGConfirmResult.Fail(
            orderId,
            paymentId
        )
        
        // when
        chargePointPaymentPostProcessor.postProcess(savedChargePointPayment, pgConfirmResult)
        
        //then
        val chargePointPayment = chargePointPaymentRepository.findByOrderId(orderId)
        Assertions.assertThat(chargePointPayment.chargePointPaymentStatus).isEqualTo(ChargePointPaymentStatus.FAILED)
    }
}