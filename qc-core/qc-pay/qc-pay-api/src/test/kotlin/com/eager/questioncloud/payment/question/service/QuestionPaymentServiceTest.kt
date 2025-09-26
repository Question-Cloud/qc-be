package com.eager.questioncloud.payment.question.service

import com.eager.questioncloud.common.event.EventPublisher
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.FixedCouponDiscount
import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import com.eager.questioncloud.payment.question.command.QuestionPaymentCommand
import com.eager.questioncloud.payment.question.implement.QuestionOrderGenerator
import com.eager.questioncloud.payment.question.implement.QuestionPaymentCouponSelector
import com.eager.questioncloud.payment.scenario.QuestionPaymentScenario
import com.eager.questioncloud.point.api.internal.PointCommandAPI
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.ints.exactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class QuestionPaymentServiceTest(
    private val questionPaymentService: QuestionPaymentService,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionOrderGenerator: QuestionOrderGenerator
    
    @MockkBean
    private lateinit var questionPaymentCouponSelector: QuestionPaymentCouponSelector
    
    @MockkBean
    private lateinit var pointCommandAPI: PointCommandAPI
    
    @MockkBean
    private lateinit var eventPublisher: EventPublisher
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("사용자가 쿠폰 없이 문제를 결제할 때") {
            val userId = 1L
            val questionPaymentScenario = QuestionPaymentScenario.create(userId, 10)
            val command = QuestionPaymentCommand(userId, questionPaymentScenario.order.questionIds, null)
            
            every { questionOrderGenerator.generateQuestionOrder(any(), any()) } returns questionPaymentScenario.order
            every {
                questionPaymentCouponSelector.select(
                    any(),
                    any()
                )
            } returns QuestionPaymentCoupon.noDiscount()
            
            justRun { eventPublisher.publish(any()) }
            justRun { pointCommandAPI.usePoint(any(), any()) }
            
            When("결제를 요청하면") {
                val paymentResult = questionPaymentService.payment(command)
                
                Then("정가로 결제가 완료되고 이벤트가 발행된다") {
                    paymentResult.amount shouldBeEqual paymentResult.order.calcAmount()
                    verify(exactly(1)) { eventPublisher.publish(any()) }
                }
            }
        }
        
        Given("사용자가 쿠폰을 사용해서 문제를 결제할 때") {
            val userId = 1L
            val questionPaymentScenario = QuestionPaymentScenario.create(userId, 10)
            val couponId = 1L
            val userCouponId = 1L
            val discountAmount = 1000
            val discountTitle = "FIXED COUPON"
            val command = QuestionPaymentCommand(userId, questionPaymentScenario.order.questionIds, userCouponId)
            val questionPaymentCoupon = QuestionPaymentCoupon(FixedCouponDiscount(discountTitle, discountAmount), couponId, userCouponId)
            
            every { questionOrderGenerator.generateQuestionOrder(any(), any()) } returns questionPaymentScenario.order
            every {
                questionPaymentCouponSelector.select(
                    any(),
                    any()
                )
            } returns questionPaymentCoupon
            justRun { eventPublisher.publish(any()) }
            justRun { pointCommandAPI.usePoint(any(), any()) }
            
            When("결제를 요청하면") {
                val paymentResult = questionPaymentService.payment(command)
                
                Then("할인된 금액으로 결제가 완료되고 이벤트가 발행된다") {
                    paymentResult.amount shouldBeEqual questionPaymentCoupon.calcDiscount(questionPaymentScenario.order.calcAmount())
                    verify(exactly(1)) { eventPublisher.publish(any()) }
                }
            }
        }
        
        Given("사용자가 포인트가 부족할 때") {
            val userId = 1L
            val questionPaymentScenario = QuestionPaymentScenario.create(userId, 10)
            val userCouponId = 1L
            val command = QuestionPaymentCommand(userId, questionPaymentScenario.order.questionIds, userCouponId)
            
            every { questionOrderGenerator.generateQuestionOrder(any(), any()) } returns questionPaymentScenario.order
            
            every {
                questionPaymentCouponSelector.select(
                    any(),
                    any()
                )
            } returns QuestionPaymentCoupon.noDiscount()
            every { pointCommandAPI.usePoint(any(), any()) } throws CoreException(Error.NOT_ENOUGH_POINT)
            
            When("결제를 요청하면") {
                Then("NOT_ENOUGH_POINT 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        questionPaymentService.payment(command)
                    }
                    exception.error shouldBe Error.NOT_ENOUGH_POINT
                }
            }
        }
    }
}