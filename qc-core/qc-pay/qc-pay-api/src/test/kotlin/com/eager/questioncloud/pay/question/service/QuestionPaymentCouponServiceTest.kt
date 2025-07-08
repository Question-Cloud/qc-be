package com.eager.questioncloud.pay.question.service

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
class QuestionPaymentCouponServiceTest(
    @Autowired val questionPaymentCouponService: QuestionPaymentCouponService,
    @Autowired val userCouponRepository: UserCouponRepository,
    @Autowired val couponRepository: CouponRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `쿠폰을 조회할 수 있다(퍼센트)`() {
        //given
        val userId = 100L
        val coupon = createCoupon("DISCOUNT10", "10% 할인 쿠폰", CouponType.Percent, 10)
        val savedCoupon = couponRepository.save(coupon)
        
        val userCoupon = createUserCoupon(userId, savedCoupon.id)
        val savedUserCoupon = userCouponRepository.save(userCoupon)
        
        //when
        val result = questionPaymentCouponService.getQuestionPaymentCoupon(savedUserCoupon.id, userId)
        
        //then
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result!!.userCouponId).isEqualTo(savedUserCoupon.id)
        Assertions.assertThat(result.couponId).isEqualTo(savedCoupon.id)
        Assertions.assertThat(result.title).isEqualTo("10% 할인 쿠폰")
        Assertions.assertThat(result.couponType).isEqualTo(CouponType.Percent)
        Assertions.assertThat(result.value).isEqualTo(10)
    }
    
    @Test
    fun `쿠폰을 조회할 수 있다(고정)`() {
        //given
        val userId = 101L
        val coupon = createCoupon("FIXED5000", "5000원 할인 쿠폰", CouponType.Fixed, 5000)
        val savedCoupon = couponRepository.save(coupon)
        
        val userCoupon = createUserCoupon(userId, savedCoupon.id)
        val savedUserCoupon = userCouponRepository.save(userCoupon)
        
        //when
        val result = questionPaymentCouponService.getQuestionPaymentCoupon(savedUserCoupon.id, userId)
        
        //then
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result!!.couponType).isEqualTo(CouponType.Fixed)
        Assertions.assertThat(result.value).isEqualTo(5000)
        Assertions.assertThat(result.title).isEqualTo("5000원 할인 쿠폰")
    }
    
    @Test
    fun `userCouponId가 null이면 null을 반환한다`() {
        //given
        val userId = 102L
        
        //when
        val result = questionPaymentCouponService.getQuestionPaymentCoupon(null, userId)
        
        //then
        Assertions.assertThat(result).isNull()
    }
    
    private fun createCoupon(code: String, title: String, couponType: CouponType, value: Int): Coupon {
        return Coupon(
            code = code,
            title = title,
            couponType = couponType,
            value = value,
            remainingCount = 100,
            endAt = LocalDateTime.now().plusDays(30)
        )
    }
    
    private fun createUserCoupon(userId: Long, couponId: Long): UserCoupon {
        return UserCoupon(
            userId = userId,
            couponId = couponId,
            isUsed = false,
            createdAt = LocalDateTime.now(),
            endAt = LocalDateTime.now().plusDays(30)
        )
    }
}
