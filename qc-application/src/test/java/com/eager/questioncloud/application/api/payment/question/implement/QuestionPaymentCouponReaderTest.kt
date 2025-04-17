package com.eager.questioncloud.application.api.payment.question.implement

import com.eager.questioncloud.application.utils.fixture.helper.CouponFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserCouponFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class QuestionPaymentCouponReaderTest(
    @Autowired val userCouponRepository: UserCouponRepository,
    @Autowired val couponRepository: CouponRepository,
    @Autowired val userRepository: UserRepository,
    @Autowired val questionPaymentCouponReader: QuestionPaymentCouponReader,
) {
    private var uid: Long = 0

    @BeforeEach
    fun setUp() {
        uid = UserFixtureHelper.createDefaultEmailUser(userRepository).uid
    }

    @AfterEach
    fun tearDown() {
        userCouponRepository.deleteAllInBatch()
        couponRepository.deleteAllInBatch()
        userRepository.deleteAllInBatch()
    }

    @Test
    fun `유효한 쿠폰을 불러 올 수 있다`() {
        // given
        val coupon = CouponFixtureHelper.createCoupon(couponRepository = couponRepository)
        val userCoupon = UserCouponFixtureHelper.createUserCoupon(
            coupon = coupon,
            uid = uid,
            userCouponRepository = userCouponRepository
        )

        // when
        val questionPaymentCoupon = questionPaymentCouponReader.getQuestionPaymentCoupon(
            userCoupon.id,
            uid
        )
        Assertions.assertThat(questionPaymentCoupon!!.userCouponId).isEqualTo(userCoupon.id)
        Assertions.assertThat(questionPaymentCoupon.title).isEqualTo(coupon.title)
        Assertions.assertThat(questionPaymentCoupon.couponType).isEqualTo(coupon.couponType)
        Assertions.assertThat(questionPaymentCoupon.value).isEqualTo(coupon.value)
    }

    @Test
    fun `존재하지 않는 쿠폰은 불러올 수 없다`() {
        // given
        val wrongUserCouponId = 12L

        //when then
        Assertions.assertThatThrownBy {
            questionPaymentCouponReader.getQuestionPaymentCoupon(
                wrongUserCouponId,
                uid
            )
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.WRONG_COUPON)
    }

    @Test
    fun `쿠폰을 사용하지 않을 수 있다`() {
        val userCouponId: Long? = null

        // when
        val questionPaymentCoupon = questionPaymentCouponReader.getQuestionPaymentCoupon(userCouponId, uid)

        // then
        Assertions.assertThat(questionPaymentCoupon).isNull()
    }
}