package com.eager.questioncloud.application.business.payment.question.implement

import com.eager.questioncloud.application.api.payment.question.implement.QuestionPaymentCouponProcessor
import com.eager.questioncloud.application.utils.Fixture
import com.eager.questioncloud.core.domain.coupon.enums.CouponType
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.core.domain.coupon.model.Coupon
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder
import com.eager.questioncloud.core.domain.payment.model.QuestionOrderItem
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment.Companion.create
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon.Companion.create
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
internal class QuestionPaymentCouponProcessorTest {
    @Autowired
    var userCouponRepository: UserCouponRepository? = null

    @Autowired
    var couponRepository: CouponRepository? = null

    @Autowired
    var userRepository: UserRepository? = null

    @Autowired
    private val questionPaymentCouponProcessor: QuestionPaymentCouponProcessor? = null

    @AfterEach
    fun tearDown() {
        userCouponRepository!!.deleteAllInBatch()
        couponRepository!!.deleteAllInBatch()
        userRepository!!.deleteAllInBatch()
    }

    @Test
    @DisplayName("쿠폰을 적용하여 금액을 할인할 수 있다.")
    fun applyCoupon() {
        // given
        val user = userRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        val coupon = couponRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
                .set(Coupon::id, null)
                .set(Coupon::couponType, CouponType.Fixed)
                .set(Coupon::value, 1000)
                .set(Coupon::endAt, LocalDateTime.now().plusDays(10))
                .sample()
        )

        val userCoupon = userCouponRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<UserCoupon>()
                .set(UserCoupon::couponId, coupon.id)
                .set(UserCoupon::userId, user.uid)
                .set(UserCoupon::endAt, coupon.endAt)
                .set(UserCoupon::isUsed, false)
                .sample()
        )

        val questionPaymentCoupon = create(userCoupon.id!!, coupon)

        val orderItems = Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionOrderItem>()
            .set(QuestionOrderItem::price, 1000)
            .sampleList(10)

        val questionOrder = QuestionOrder(UUID.randomUUID().toString(), orderItems)

        val questionPayment = create(user.uid!!, questionPaymentCoupon, questionOrder)
        val originalAmount = questionPayment.amount

        // when
        questionPaymentCouponProcessor!!.applyCoupon(questionPayment)

        // then
        val afterAmount = questionPayment.amount
        val saleAmount = originalAmount - afterAmount

        Assertions.assertThat(saleAmount).isEqualTo(questionPaymentCoupon.value)
    }
}