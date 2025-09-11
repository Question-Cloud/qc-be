package com.eager.questioncloud.point.implement

import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.domain.UserPoint
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
        userPointRepository.save(UserPoint.create(userId))
        
        val chargeType = ChargePointType.PackageC
        val chargePointPayment = ChargePointPayment.createOrder(userId, chargeType)
        chargePointPayment.charge()
        chargePointPaymentRepository.save(chargePointPayment)
        
        // when
        chargePointPaymentPostProcessor.postProcess(chargePointPayment)
        
        //then
        val userPoint = userPointRepository.getUserPoint(userId)
        Assertions.assertThat(userPoint.point).isEqualTo(chargeType.amount)
    }
}