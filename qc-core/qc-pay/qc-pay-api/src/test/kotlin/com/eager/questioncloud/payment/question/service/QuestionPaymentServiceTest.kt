package com.eager.questioncloud.payment.question.service

import com.eager.questioncloud.common.event.EventPublisher
import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.domain.QuestionOrderItem
import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import com.eager.questioncloud.payment.scenario.CouponScenario
import com.eager.questioncloud.payment.scenario.setUserCoupon
import com.eager.questioncloud.point.api.internal.PointCommandAPI
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.ints.exactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.justRun
import io.mockk.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class QuestionPaymentServiceTest(
    private val questionPaymentService: QuestionPaymentService,
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var pointCommandAPI: PointCommandAPI
    
    @MockkBean
    private lateinit var eventPublisher: EventPublisher
    
    private fun createQuestionOrder(orderItems: List<QuestionOrderItem>): QuestionOrder {
        return QuestionOrder.createOrder(orderItems)
    }
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("사용자가 쿠폰 없이 문제를 결제할 때") {
            val userId = 1L
            val order = createQuestionOrder(
                listOf(
                    QuestionOrderItem(questionId = 200L, price = 10000),
                    QuestionOrderItem(questionId = 201L, price = 15000)
                )
            )
            
            justRun { pointCommandAPI.usePoint(userId, order.calcAmount()) }
            justRun { eventPublisher.publish(any()) }
            
            When("결제를 요청하면") {
                val result = questionPaymentService.payment(userId, order, null)
                
                Then("정가로 결제가 완료되고 이벤트가 발행된다") {
                    result shouldNotBe null
                    result.userId shouldBe userId
                    result.order.orderId shouldBe order.orderId
                    result.amount shouldBe order.calcAmount()
                    result.questionPaymentCoupon shouldBe null
                    result.isUsedCoupon() shouldBe false
                    
                    verify(exactly(1)) { pointCommandAPI.usePoint(userId, order.calcAmount()) }
                    verify(exactly(1)) { eventPublisher.publish(any()) }
                }
            }
        }
        
        Given("사용자가 퍼센트 할인 쿠폰으로 문제를 결제할 때") {
            val userId = 1L
            val order = createQuestionOrder(
                listOf(QuestionOrderItem(questionId = 202L, price = 20000))
            )
            
            val coupon = CouponScenario.available(1, couponRepository, CouponType.Percent).coupons[0]
            val userCoupon = coupon.setUserCoupon(userId, userCouponRepository)
            val paymentCoupont = QuestionPaymentCoupon.create(userCoupon.id, coupon)
            
            justRun { pointCommandAPI.usePoint(any(), any()) }
            justRun { eventPublisher.publish(any()) }
            
            When("결제를 요청하면") {
                val result = questionPaymentService.payment(userId, order, paymentCoupont)
                
                Then("할인된 금액으로 결제가 완료된다") {
                    result shouldNotBe null
                    result.userId shouldBe userId
                    result.amount shouldBe paymentCoupont.calcDiscount(order.calcAmount())
                    result.questionPaymentCoupon shouldNotBe null
                    result.isUsedCoupon() shouldBe true
                    
                    verify(exactly(1)) { pointCommandAPI.usePoint(userId, paymentCoupont.calcDiscount(order.calcAmount())) }
                    verify(exactly(1)) { eventPublisher.publish(any()) }
                }
            }
        }
        
        Given("사용자가 고정 할인 쿠폰으로 문제를 결제할 때") {
            val userId = 1L
            val order = createQuestionOrder(
                listOf(QuestionOrderItem(questionId = 203L, price = 15000))
            )
            
            val coupon = CouponScenario.available(1, couponRepository).coupons[0]
            val userCoupon = coupon.setUserCoupon(userId, userCouponRepository)
            val paymentCoupon = QuestionPaymentCoupon.create(userCoupon.id, coupon)
            
            justRun { pointCommandAPI.usePoint(any(), any()) }
            justRun { eventPublisher.publish(any()) }
            
            When("결제를 요청하면") {
                val result = questionPaymentService.payment(userId, order, paymentCoupon)
                
                Then("고정 할인 금액이 적용되어 결제가 완료된다") {
                    result shouldNotBe null
                    result.userId shouldBe userId
                    result.amount shouldBe paymentCoupon.calcDiscount(order.calcAmount())
                    result.questionPaymentCoupon shouldNotBe null
                    result.isUsedCoupon() shouldBe true
                    result.questionPaymentCoupon!!.couponType shouldBe CouponType.Fixed
                    
                    verify { pointCommandAPI.usePoint(userId, paymentCoupon.calcDiscount(order.calcAmount())) }
                    verify { eventPublisher.publish(any()) }
                }
            }
        }
    }
}