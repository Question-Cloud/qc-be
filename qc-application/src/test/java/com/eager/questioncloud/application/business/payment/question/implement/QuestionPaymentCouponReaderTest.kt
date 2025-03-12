package com.eager.questioncloud.application.business.payment.question.implement

import com.eager.questioncloud.application.api.payment.question.implement.QuestionPaymentCouponReader
import com.eager.questioncloud.application.utils.Fixture
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.core.domain.coupon.model.Coupon
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
internal class QuestionPaymentCouponReaderTest {
    @Autowired
    var userCouponRepository: UserCouponRepository? = null

    @Autowired
    var couponRepository: CouponRepository? = null

    @Autowired
    var userRepository: UserRepository? = null

    @Autowired
    var questionPaymentCouponReader: QuestionPaymentCouponReader? = null

    @AfterEach
    fun tearDown() {
        userCouponRepository!!.deleteAllInBatch()
        couponRepository!!.deleteAllInBatch()
        userRepository!!.deleteAllInBatch()
    }

    @DisplayName("유효한 쿠폰을 불러 올 수 있다.")
    @Test
    fun getCoupon() {
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

        // when
        val questionPaymentCoupon = questionPaymentCouponReader!!.getQuestionPaymentCoupon(
            userCoupon.id,
            user.uid!!
        )
        Assertions.assertThat(questionPaymentCoupon!!.userCouponId).isEqualTo(userCoupon.id)
        Assertions.assertThat(questionPaymentCoupon.title).isEqualTo(coupon.title)
        Assertions.assertThat(questionPaymentCoupon.couponType).isEqualTo(coupon.couponType)
        Assertions.assertThat(questionPaymentCoupon.value).isEqualTo(coupon.value)
    }

    @Test
    @DisplayName("존재하지 않는 쿠폰은 불러올 수 없다.")
    fun cannotGetWrongCoupon() {
        // given
        val user = userRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        val wrongUserCouponId = 12L

        //when then
        Assertions.assertThatThrownBy {
            questionPaymentCouponReader!!.getQuestionPaymentCoupon(
                wrongUserCouponId,
                user.uid!!
            )
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.WRONG_COUPON)
    }

    @Test
    @DisplayName("쿠폰을 사용하지 않을 수 있다.")
    fun noCoupon() {
        val userId = 1L
        val userCouponId: Long? = null

        // when
        val questionPaymentCoupon = questionPaymentCouponReader!!.getQuestionPaymentCoupon(userCouponId, userId)

        // then
        Assertions.assertThat(questionPaymentCoupon).isNull()
    }
}