package com.eager.questioncloud.application.api.payment.point.implement

import com.eager.questioncloud.application.utils.UserFixtureHelper
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import com.eager.questioncloud.core.domain.point.model.UserPoint
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class ChargePointPaymentPostProcessorTest(
    @Autowired val userRepository: UserRepository,
    @Autowired val userPointRepository: UserPointRepository,
    @Autowired val chargePointPaymentRepository: ChargePointPaymentRepository,
    @SpyBean @Autowired val chargePointPaymentPostProcessor: ChargePointPaymentPostProcessor,
) {
    private var uid: Long = 0

    @BeforeEach
    fun setUp() {
        uid = UserFixtureHelper.createDefaultEmailUser(userRepository).uid
    }

    @AfterEach
    fun tearDown() {
        userRepository.deleteAllInBatch()
        userPointRepository.deleteAllInBatch()
        chargePointPaymentRepository.deleteAllInBatch()
    }

    @Test
    fun `포인트 충전을 처리할 수 있다`() {
        // given
        userPointRepository.save(UserPoint(uid, 0))

        val paymentId = RandomStringUtils.randomAlphanumeric(10)

        val payment =
            chargePointPaymentRepository.save(ChargePointPayment.createOrder(uid, ChargePointType.PackageA))

        payment.prepare(paymentId)

        val chargePointPayment = chargePointPaymentRepository.save(payment)

        // when
        chargePointPaymentPostProcessor.chargeUserPoint(chargePointPayment)

        //then
        val userPoint = userPointRepository.getUserPoint(uid)
        Assertions.assertThat(userPoint.point).isEqualTo(ChargePointType.PackageA.amount)
    }
}