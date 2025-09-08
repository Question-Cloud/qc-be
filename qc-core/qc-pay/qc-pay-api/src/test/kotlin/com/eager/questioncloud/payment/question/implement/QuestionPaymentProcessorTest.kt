package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.*
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.QuestionPaymentRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import com.eager.questioncloud.point.api.internal.PointCommandAPI
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.given
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class QuestionPaymentProcessorTest(
    @Autowired val questionPaymentProcessor: QuestionPaymentProcessor,
    @Autowired val couponRepository: CouponRepository,
    @Autowired val userCouponRepository: UserCouponRepository,
    @Autowired val questionPaymentRepository: QuestionPaymentRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var pointCommandAPI: PointCommandAPI
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `문제 결제 처리를 할 수 있다 (쿠폰 O)`() {
        // given
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
        
        val orderItems = listOf(
            QuestionOrderItem(questionId = 1L, price = 1000),
            QuestionOrderItem(questionId = 2L, price = 1000),
            QuestionOrderItem(questionId = 3L, price = 1000)
        )
        val questionOrder = QuestionOrder(UUID.randomUUID().toString(), orderItems)
        val questionPaymentCoupon = QuestionPaymentCoupon.create(userCoupon.id, fixedCoupon)
        val questionPayment = QuestionPayment.create(userId, questionPaymentCoupon, questionOrder)
        
        val originalAmount = questionPayment.amount
        val discountedAmount = questionPayment.questionPaymentCoupon!!.calcDiscount(originalAmount)
        
        doNothing().whenever(pointCommandAPI).usePoint(any(), any())
        // when
        questionPaymentProcessor.payment(questionPayment)
        
        // then
        val afterUserCoupon = userCouponRepository.getUserCoupon(userCoupon.id)
        assertThat(afterUserCoupon.isUsed).isTrue()
        assertThat(questionPayment.amount).isEqualTo(discountedAmount)
        
        val paymentCount = questionPaymentRepository.countByUserId(userId)
        assertThat(paymentCount).isEqualTo(1)
    }
    
    @Test
    fun `문제 결제 처리를 할 수 있다 (쿠폰 X)`() {
        // given
        val userId = 1L
        
        val orderItems = listOf(
            QuestionOrderItem(questionId = 1L, price = 1000),
            QuestionOrderItem(questionId = 2L, price = 1000),
            QuestionOrderItem(questionId = 3L, price = 1000)
        )
        
        val questionOrder = QuestionOrder(UUID.randomUUID().toString(), orderItems)
        val questionPayment = QuestionPayment.create(userId, null, questionOrder)
        
        doNothing().whenever(pointCommandAPI).usePoint(any(), any())
        
        // when
        questionPaymentProcessor.payment(questionPayment)
        
        // then
        val paymentCount = questionPaymentRepository.countByUserId(userId)
        assertThat(paymentCount).isEqualTo(1)
    }
    
    @Test
    fun `보유 포인트가 부족하면 포인트 부족 예외가 발생한다`() {
        // given
        val userId = 1L
        
        val orderItems = listOf(
            QuestionOrderItem(questionId = 1L, price = 1000),
            QuestionOrderItem(questionId = 2L, price = 1000),
            QuestionOrderItem(questionId = 3L, price = 1000)
        )
        
        val questionOrder = QuestionOrder(UUID.randomUUID().toString(), orderItems)
        val questionPayment = QuestionPayment.create(userId, null, questionOrder)
        
        given(
            pointCommandAPI.usePoint(
                any(),
                any()
            )
        ).willThrow(CoreException(Error.NOT_ENOUGH_POINT))
        
        // when
        assertThatThrownBy {
            questionPaymentProcessor.payment(questionPayment)
        }.isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.NOT_ENOUGH_POINT)
    }
}