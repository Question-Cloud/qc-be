package com.eager.questioncloud.pay.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.Coupon
import com.eager.questioncloud.payment.domain.UserCoupon
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.infrastructure.repository.CouponRepository
import com.eager.questioncloud.payment.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
class QuestionPaymentCouponReaderTest(
    @Autowired val userCouponRepository: UserCouponRepository,
    @Autowired val couponRepository: CouponRepository,
    @Autowired val questionPaymentCouponReader: QuestionPaymentCouponReader,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `쿠폰을 불러 올 수 있다`() {
        // given
        val userId = 1L
        val coupon = couponRepository.save(
            Coupon(
                code = "coupon-code",
                title = "할인 쿠폰 1000",
                couponType = CouponType.Fixed,
                value = 1000,
                remainingCount = 100,
                endAt = LocalDateTime.now().plusDays(10)
            )
        )
        val userCoupon = userCouponRepository.save(UserCoupon.create(userId, coupon))
        
        // when
        val questionPaymentCoupon = questionPaymentCouponReader.getQuestionPaymentCoupon(
            userCoupon.id,
            userId
        )
        
        Assertions.assertThat(questionPaymentCoupon!!.userCouponId).isEqualTo(userCoupon.id)
        Assertions.assertThat(questionPaymentCoupon.title).isEqualTo(coupon.title)
        Assertions.assertThat(questionPaymentCoupon.couponType).isEqualTo(coupon.couponType)
        Assertions.assertThat(questionPaymentCoupon.value).isEqualTo(coupon.value)
    }
    
    @Test
    fun `만료된 쿠폰은 불러올 수 없다`() {
        // given
        val userId = 1L
        val coupon = couponRepository.save(
            Coupon(
                code = "coupon-code",
                title = "할인 쿠폰 1000",
                couponType = CouponType.Fixed,
                value = 1000,
                remainingCount = 100,
                endAt = LocalDateTime.now().minusDays(10)
            )
        )
        val userCoupon = userCouponRepository.save(
            UserCoupon(
                userId = userId,
                couponId = coupon.id,
                isUsed = false,
                createdAt = LocalDateTime.now(),
                endAt = coupon.endAt,
            )
        )
        
        // when then
        Assertions.assertThatThrownBy { questionPaymentCouponReader.getQuestionPaymentCoupon(userCoupon.id, userId) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.EXPIRED_COUPON)
    }
    
    @Test
    fun `이미 사용한 쿠폰은 불러올 수 없다`() {
        // given
        val userId = 1L
        val coupon = couponRepository.save(
            Coupon(
                code = "coupon-code",
                title = "할인 쿠폰 1000",
                couponType = CouponType.Fixed,
                value = 1000,
                remainingCount = 100,
                endAt = LocalDateTime.now().minusDays(10)
            )
        )
        val userCoupon = userCouponRepository.save(
            UserCoupon(
                userId = userId,
                couponId = coupon.id,
                isUsed = true,
                createdAt = LocalDateTime.now(),
                endAt = coupon.endAt,
            )
        )
        
        // when then
        Assertions.assertThatThrownBy { questionPaymentCouponReader.getQuestionPaymentCoupon(userCoupon.id, userId) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.WRONG_COUPON)
    }
    
    @Test
    fun `쿠폰을 사용하지 않을 수 있다`() {
        val userId = 1L
        val userCouponId: Long? = null
        
        // when
        val questionPaymentCoupon = questionPaymentCouponReader.getQuestionPaymentCoupon(userCouponId, userId)
        
        // then
        Assertions.assertThat(questionPaymentCoupon).isNull()
    }
}