package com.eager.questioncloud.application.api.payment.question.implement

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.Fixture
import com.eager.questioncloud.application.utils.fixture.helper.CouponFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserCouponFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.coupon.enums.CouponType
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder
import com.eager.questioncloud.core.domain.payment.model.QuestionOrderItem
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment.Companion.create
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*
import kotlin.math.floor

@SpringBootTest
@ActiveProfiles("test")
internal class QuestionPaymentCouponProcessorTest(
    @Autowired val userCouponRepository: UserCouponRepository,
    @Autowired val couponRepository: CouponRepository,
    @Autowired val userRepository: UserRepository,
    @Autowired val questionPaymentCouponProcessor: QuestionPaymentCouponProcessor,
    @Autowired val dbCleaner: DBCleaner,
) {
    private var uid: Long = 0

    @BeforeEach
    fun setUp() {
        uid = UserFixtureHelper.createDefaultEmailUser(userRepository).uid
    }

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `고정 할인 쿠폰을 적용할 수 있다`() {
        // given
        val discount = 1000
        val fixedCoupon = CouponFixtureHelper.createCoupon(
            couponType = CouponType.Fixed,
            value = discount,
            couponRepository = couponRepository
        )
        val userCoupon = UserCouponFixtureHelper.createUserCoupon(fixedCoupon, uid, userCouponRepository)
        val questionPaymentCoupon = QuestionPaymentCoupon.create(userCoupon.id, fixedCoupon)
        val orderItems = dummeyQuestionOrders()
        val questionOrder = QuestionOrder(UUID.randomUUID().toString(), orderItems)
        val questionPayment = create(uid, questionPaymentCoupon, questionOrder)
        val originalAmount = questionPayment.amount

        // when
        questionPaymentCouponProcessor.applyCoupon(questionPayment)

        // then
        val afterAmount = questionPayment.amount
        val saleAmount = originalAmount - afterAmount

        Assertions.assertThat(saleAmount).isEqualTo(questionPaymentCoupon.value)
    }


    @Test
    fun `비율 할인 쿠폰을 적용할 수 있다 (calcDiscount 로직 반영)`() {
        // given
        val discountPercentage = 10
        val percentCoupon = CouponFixtureHelper.createCoupon(
            couponType = CouponType.Percent,
            value = discountPercentage,
            couponRepository = couponRepository
        )
        val userCoupon = UserCouponFixtureHelper.createUserCoupon(percentCoupon, uid, userCouponRepository)
        val questionPaymentCoupon = QuestionPaymentCoupon.create(userCoupon.id, percentCoupon)
        val orderItems = dummeyQuestionOrders()
        val questionOrder = QuestionOrder(UUID.randomUUID().toString(), orderItems)
        val questionPayment = create(uid, questionPaymentCoupon, questionOrder)
        val originalAmount: Int = questionPayment.amount

        // when
        questionPaymentCouponProcessor.applyCoupon(questionPayment)

        // then
        val afterAmount: Int = questionPayment.amount
        val saleAmount: Int = originalAmount - afterAmount

        val expectedDiscountAmount = floor(originalAmount.toDouble() * (discountPercentage.toDouble() / 100.0)).toInt()
        Assertions.assertThat(saleAmount)
            .describedAs("할인 금액 검증: 원금($originalAmount)의 $discountPercentage% (버림 적용)")
            .isEqualTo(expectedDiscountAmount)
    }

    private fun dummeyQuestionOrders(): List<QuestionOrderItem> {
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionOrderItem>()
            .set(QuestionOrderItem::price, 1000)
            .sampleList(10)
    }
}