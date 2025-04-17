package com.eager.questioncloud.application.api.payment.point.implement

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.ChargePointPaymentFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.eager.questioncloud.core.exception.InvalidPaymentException
import com.eager.questioncloud.pg.dto.PGPayment
import com.eager.questioncloud.pg.toss.PaymentStatus
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
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
class ChargePointPaymentPreparerTest(
    @Autowired val chargePointPaymentPreparer: ChargePointPaymentPreparer,
    @Autowired val userRepository: UserRepository,
    @SpyBean @Autowired val chargePointPaymentRepository: ChargePointPaymentRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    private var uid: Long = 0

    @BeforeEach
    fun setUp() {
        uid = UserFixtureHelper.createDefaultSocialUser(userRepository).uid
    }

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `포인트 결제 검증 성공`() {
        //given
        val paymentId = RandomStringUtils.randomAlphanumeric(10)
        val order = ChargePointPaymentFixtureHelper.createChargePointPayment(
            uid = uid,
            chargePointType = ChargePointType.PackageA,
            chargePointPaymentRepository = chargePointPaymentRepository,
        )
        val pgPayment = PGPayment(paymentId, order.orderId, ChargePointType.PackageA.amount, PaymentStatus.DONE)

        //when
        chargePointPaymentPreparer.prepare(pgPayment)

        //then
        val chargePointPayment = chargePointPaymentRepository.findByOrderId(order.orderId)
        Assertions.assertThat(chargePointPayment.chargePointPaymentStatus)
            .isEqualTo(ChargePointPaymentStatus.PAYMENT_REQUEST)
    }

    @Test
    fun `결제 금액이 올바르지 않으면 예외가 발생한다`() {
        //given
        val paymentId = RandomStringUtils.randomAlphanumeric(10)
        val order = ChargePointPaymentFixtureHelper.createChargePointPayment(
            uid = uid,
            chargePointType = ChargePointType.PackageA,
            chargePointPaymentRepository = chargePointPaymentRepository,
        )
        val wrongPaymentAmount = order.chargePointType.amount - 500
        val pgPayment = PGPayment(paymentId, order.orderId, wrongPaymentAmount, PaymentStatus.DONE)

        //when then
        Assertions.assertThatThrownBy { chargePointPaymentPreparer.prepare(pgPayment) }
            .isInstanceOf(InvalidPaymentException::class.java)
    }


    @Test
    fun `이미 결제 처리가 완료된 경우 예외가 발생한다`() {
        //given
        val paymentId = RandomStringUtils.randomAlphanumeric(10)
        val order = ChargePointPaymentFixtureHelper.createChargePointPayment(
            uid = uid,
            chargePointType = ChargePointType.PackageA,
            chargePointPaymentRepository = chargePointPaymentRepository,
            chargePointPaymentStatus = ChargePointPaymentStatus.CHARGED,
        )
        val pgPayment = PGPayment(paymentId, order.orderId, ChargePointType.PackageA.amount, PaymentStatus.DONE)

        //when then
        Assertions.assertThatThrownBy { chargePointPaymentPreparer.prepare(pgPayment) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_PROCESSED_PAYMENT)
    }

    @Test
    fun `결제 준비 요청 동시성 이슈를 방지할 수 있다`() {
        //given
        val paymentId = RandomStringUtils.randomAlphanumeric(10)
        val order = ChargePointPaymentFixtureHelper.createChargePointPayment(
            uid = uid,
            chargePointType = ChargePointType.PackageA,
            chargePointPaymentRepository = chargePointPaymentRepository,
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
                    chargePointPaymentPreparer.prepare(pgPayment)
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