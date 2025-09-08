package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.domain.*
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.*
import kotlin.math.floor

@SpringBootTest
@ActiveProfiles("test")
class QuestionPaymentCouponProcessorTest(
    @Autowired val userCouponRepository: UserCouponRepository,
    @Autowired val couponRepository: CouponRepository,
    @Autowired val questionPaymentCouponProcessor: QuestionPaymentCouponProcessor,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `고정 할인 쿠폰을 적용할 수 있다`() {
        // given\
        val userId = 1L
        val discount = 1000
        val fixedCoupon = couponRepository.save(
            Coupon(
                code = "coupon-code",
                title = "할인 쿠폰 1000",
                couponType = CouponType.Fixed,
                value = discount,
                remainingCount = 100,
                endAt = LocalDateTime.now().plusDays(10)
            )
        )
        val userCoupon = userCouponRepository.save(UserCoupon.create(userId, fixedCoupon))
        val questionPaymentCoupon = QuestionPaymentCoupon.create(userCoupon.id, fixedCoupon)
        val orderItems = listOf(
            QuestionOrderItem(questionId = 1L, price = 1000),
            QuestionOrderItem(questionId = 2L, price = 1000),
            QuestionOrderItem(questionId = 3L, price = 1000)
        )
        val questionOrder = QuestionOrder(UUID.randomUUID().toString(), orderItems)
        val questionPayment = QuestionPayment.create(userId, questionPaymentCoupon, questionOrder)
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
        val userId = 1L
        val discountPercentage = 10
        val percentCoupon = couponRepository.save(
            Coupon(
                code = "coupon-code",
                title = "할인 쿠폰 1000",
                couponType = CouponType.Percent,
                value = discountPercentage,
                remainingCount = 100,
                endAt = LocalDateTime.now().plusDays(10)
            )
        )
        val userCoupon = userCouponRepository.save(UserCoupon.create(userId, percentCoupon))
        val questionPaymentCoupon = QuestionPaymentCoupon.create(userCoupon.id, percentCoupon)
        val orderItems = listOf(
            QuestionOrderItem(questionId = 1L, price = 1000),
            QuestionOrderItem(questionId = 2L, price = 1000),
            QuestionOrderItem(questionId = 3L, price = 1000)
        )
        val questionOrder = QuestionOrder(UUID.randomUUID().toString(), orderItems)
        val questionPayment = QuestionPayment.create(userId, questionPaymentCoupon, questionOrder)
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
}