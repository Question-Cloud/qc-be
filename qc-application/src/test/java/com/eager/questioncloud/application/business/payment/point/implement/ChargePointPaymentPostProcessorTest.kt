package com.eager.questioncloud.application.business.payment.point.implement

import com.eager.questioncloud.application.api.payment.point.implement.ChargePointPaymentPostProcessor
import com.eager.questioncloud.application.utils.Fixture
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import com.eager.questioncloud.core.domain.point.model.UserPoint
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class ChargePointPaymentPostProcessorTest {
    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val userPointRepository: UserPointRepository? = null

    @Autowired
    private val chargePointPaymentRepository: ChargePointPaymentRepository? = null

    @SpyBean
    @Autowired
    private val chargePointPaymentPostProcessor: ChargePointPaymentPostProcessor? = null

    @AfterEach
    fun tearDown() {
        userRepository!!.deleteAllInBatch()
        userPointRepository!!.deleteAllInBatch()
        chargePointPaymentRepository!!.deleteAllInBatch()
    }

    @Test
    @DisplayName("포인트 충전을 처리할 수 있다.")
    fun chargeUserPoint() {
        // given
        val user = userRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )
        userPointRepository!!.save(UserPoint(user.uid!!, 0))

        val paymentId = RandomStringUtils.randomAlphanumeric(10)

        val payment =
            chargePointPaymentRepository!!.save(ChargePointPayment.createOrder(user.uid!!, ChargePointType.PackageA))
        
        payment.approve(paymentId)

        val chargePointPayment = chargePointPaymentRepository.save(payment)

        // when
        chargePointPaymentPostProcessor!!.chargeUserPoint(chargePointPayment)

        //then
        val userPoint = userPointRepository.getUserPoint(user.uid!!)
        Assertions.assertThat(userPoint.point).isEqualTo(ChargePointType.PackageA.amount)
    }
}