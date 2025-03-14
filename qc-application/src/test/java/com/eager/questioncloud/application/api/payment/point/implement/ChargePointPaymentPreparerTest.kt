package com.eager.questioncloud.application.api.payment.point.implement

import com.eager.questioncloud.application.utils.Fixture
import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import com.eager.questioncloud.core.domain.point.model.UserPoint
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.eager.questioncloud.pg.dto.PGPayment
import com.eager.questioncloud.pg.toss.PaymentStatus
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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
@ActiveProfiles("test")
internal class ChargePointPaymentPreparerTest {
    @Autowired
    private val chargePointPaymentPreparer: ChargePointPaymentPreparer? = null

    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val userPointRepository: UserPointRepository? = null

    @SpyBean
    @Autowired
    private val failChargePointPaymentEventProcessor: FailChargePointPaymentEventProcessor? = null

    @SpyBean
    @Autowired
    private val chargePointPaymentRepository: ChargePointPaymentRepository? = null

    @AfterEach
    fun tearDown() {
        userRepository!!.deleteAllInBatch()
        userPointRepository!!.deleteAllInBatch()
        chargePointPaymentRepository!!.deleteAllInBatch()
    }

    @Test
    @DisplayName("포인트 충전 결제 승인을 할 수 있다.")
    fun prepare() {
        //given
        val user = userRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )
        userPointRepository!!.save(UserPoint(user.uid!!, 0))

        val paymentId = RandomStringUtils.randomAlphanumeric(10)

        val order = chargePointPaymentRepository!!.save(
            ChargePointPayment.createOrder(
                user.uid!!, ChargePointType.PackageA
            )
        )

        val pgPayment = PGPayment(paymentId, order.orderId, ChargePointType.PackageA.amount, PaymentStatus.DONE)

        //when
        chargePointPaymentPreparer!!.prepare(pgPayment)

        //then
        val chargePointPayment = chargePointPaymentRepository.findByOrderId(order.orderId)
        Assertions.assertThat(chargePointPayment.chargePointPaymentStatus).isEqualTo(ChargePointPaymentStatus.PAID)
    }


    @Test
    @DisplayName("이미 처리 된 결제를 승인 요청할 경우 예외가 발생한다.")
    fun throwExceptionWhenAlreadyApproved() {
        //given
        val user = userRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )
        userPointRepository!!.save(UserPoint(user.uid!!, 0))

        val paymentId = RandomStringUtils.randomAlphanumeric(10)

        val order = ChargePointPayment.createOrder(user.uid!!, ChargePointType.PackageA)
        order.prepare(paymentId)

        chargePointPaymentRepository!!.save(order)

        val pgPayment = PGPayment(paymentId, order.orderId, ChargePointType.PackageA.amount, PaymentStatus.DONE)

        //when then
        Assertions.assertThatThrownBy { chargePointPaymentPreparer!!.prepare(pgPayment) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_PROCESSED_PAYMENT)
    }

    @Test
    @DisplayName("결제 승인 요청 동시성 이슈를 방지할 수 있다.")
    @Throws(
        InterruptedException::class
    )
    fun preventPrepareConcurrency() {
        //given
        val user = userRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )
        userPointRepository!!.save(UserPoint(user.uid!!, 0))

        val paymentId = RandomStringUtils.randomAlphanumeric(10)

        val order = chargePointPaymentRepository!!.save(
            ChargePointPayment.createOrder(user.uid!!, ChargePointType.PackageA)
        )

        val pgPayment = PGPayment(paymentId, order.orderId, ChargePointType.PackageA.amount, PaymentStatus.DONE)

        //when
        val numberOfThreads = 100
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)
        val successCount = AtomicInteger(0)
        val failureCount = AtomicInteger(0)

        //when
        for (i in 0..<numberOfThreads) {
            executorService.execute {
                try {
                    chargePointPaymentPreparer!!.prepare(pgPayment)
                    successCount.incrementAndGet()
                } catch (e: CoreException) {
                    if (e.error == Error.ALREADY_PROCESSED_PAYMENT) {
                        failureCount.incrementAndGet()
                    }
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        executorService.shutdown()

        //then
        Assertions.assertThat(failureCount.get()).isEqualTo(99)
        Assertions.assertThat(successCount.get()).isEqualTo(1)
    }
}