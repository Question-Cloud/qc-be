package com.eager.questioncloud.pay.question.service

import com.eager.questioncloud.event.implement.EventPublisher
import com.eager.questioncloud.payment.domain.*
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.infrastructure.repository.CouponRepository
import com.eager.questioncloud.payment.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.point.api.internal.PointCommandAPI
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
class QuestionPaymentServiceTest(
    @Autowired val questionPaymentService: QuestionPaymentService,
    @Autowired val couponRepository: CouponRepository,
    @Autowired val userCouponRepository: UserCouponRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var pointCommandAPI: PointCommandAPI
    
    @MockBean
    lateinit var eventPublisher: EventPublisher
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `쿠폰 없이 문제 결제를 할 수 있다`() {
        //given
        val userId = 100L
        val order = createQuestionOrder(
            listOf(
                QuestionOrderItem(questionId = 200L, price = 10000),
                QuestionOrderItem(questionId = 201L, price = 15000)
            )
        )
        
        doNothing().`when`(pointCommandAPI).usePoint(userId, 25000)
        doNothing().`when`(eventPublisher).saveEventTicket(any())
        
        //when
        val result = questionPaymentService.payment(userId, order, null)
        
        //then
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.userId).isEqualTo(userId)
        Assertions.assertThat(result.order.orderId).isEqualTo(order.orderId)
        Assertions.assertThat(result.amount).isEqualTo(25000)
        Assertions.assertThat(result.questionPaymentCoupon).isNull()
        Assertions.assertThat(result.isUsedCoupon()).isFalse()
        
        verify(pointCommandAPI).usePoint(userId, 25000)
        verify(eventPublisher).saveEventTicket(any())
    }
    
    @Test
    fun `퍼센트 할인 쿠폰으로 문제 결제를 할 수 있다`() {
        //given
        val userId = 101L
        val order = createQuestionOrder(
            listOf(
                QuestionOrderItem(questionId = 202L, price = 20000)
            )
        )
        
        val discountPercentage = 10
        val percentCoupon = couponRepository.save(
            Coupon(
                code = "coupon-code",
                title = "10% 할인",
                couponType = CouponType.Percent,
                value = discountPercentage,
                remainingCount = 100,
                endAt = LocalDateTime.now().plusDays(10)
            )
        )
        val userCoupon = userCouponRepository.save(UserCoupon.create(userId, percentCoupon))
        val coupon = QuestionPaymentCoupon.create(userCoupon.id, percentCoupon)
        
        doNothing().`when`(pointCommandAPI).usePoint(userId, 18000)
        doNothing().`when`(eventPublisher).saveEventTicket(any())
        
        //when
        val result = questionPaymentService.payment(userId, order, coupon)
        
        //then
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.userId).isEqualTo(userId)
        Assertions.assertThat(result.amount).isEqualTo(18000)
        Assertions.assertThat(result.questionPaymentCoupon).isNotNull
        Assertions.assertThat(result.isUsedCoupon()).isTrue()
        Assertions.assertThat(result.questionPaymentCoupon!!.title).isEqualTo("10% 할인")
        
        verify(pointCommandAPI).usePoint(userId, 18000)
        verify(eventPublisher).saveEventTicket(any())
    }
    
    @Test
    fun `고정 할인 쿠폰으로 문제 결제를 할 수 있다`() {
        //given
        val userId = 102L
        val order = createQuestionOrder(
            listOf(
                QuestionOrderItem(questionId = 203L, price = 15000)
            )
        )
        val discount = 5000
        val fixedCoupon = couponRepository.save(
            Coupon(
                code = "coupon-code",
                title = "할인 쿠폰 5000",
                couponType = CouponType.Fixed,
                value = discount,
                remainingCount = 100,
                endAt = LocalDateTime.now().plusDays(10)
            )
        )
        val userCoupon = userCouponRepository.save(UserCoupon.create(userId, fixedCoupon))
        val coupon = QuestionPaymentCoupon.create(userCoupon.id, fixedCoupon)
        
        doNothing().`when`(pointCommandAPI).usePoint(userId, 10000)
        doNothing().`when`(eventPublisher).saveEventTicket(any())
        
        //when
        val result = questionPaymentService.payment(userId, order, coupon)
        
        //then
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.userId).isEqualTo(userId)
        Assertions.assertThat(result.amount).isEqualTo(10000) // 15000 - 5000 = 10000
        Assertions.assertThat(result.questionPaymentCoupon).isNotNull
        Assertions.assertThat(result.isUsedCoupon()).isTrue()
        Assertions.assertThat(result.questionPaymentCoupon!!.couponType).isEqualTo(CouponType.Fixed)
        
        verify(pointCommandAPI).usePoint(userId, 10000)
        verify(eventPublisher).saveEventTicket(any())
    }
    
    private fun createQuestionOrder(orderItems: List<QuestionOrderItem>): QuestionOrder {
        return QuestionOrder.createOrder(orderItems)
    }
}
