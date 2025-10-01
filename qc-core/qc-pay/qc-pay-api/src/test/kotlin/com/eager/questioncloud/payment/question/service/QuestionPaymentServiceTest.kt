package com.eager.questioncloud.payment.question.service

import com.eager.questioncloud.common.event.EventPublisher
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.question.command.QuestionOrderCommand
import com.eager.questioncloud.payment.question.command.QuestionPaymentCommand
import com.eager.questioncloud.payment.question.implement.*
import com.eager.questioncloud.payment.scenario.QuestionOrderScenario
import com.eager.questioncloud.point.api.internal.PointCommandAPI
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
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
    private lateinit var promotionApplier: PromotionApplier
    
    @MockkBean
    private lateinit var orderCouponApplier: OrderCouponApplier
    
    @MockkBean
    private lateinit var paymentCouponApplier: PaymentCouponApplier
    
    @MockkBean
    private lateinit var questionPaymentRecorder: QuestionPaymentRecorder
    
    @MockkBean
    private lateinit var pointCommandAPI: PointCommandAPI
    
    @MockkBean
    private lateinit var eventPublisher: EventPublisher
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("문제 결제") {
            val userId = 1L
            val questionOrder = QuestionOrderScenario.create(5)
            val questionOrderCommand = questionOrder.items.map { QuestionOrderCommand(it.questionInfo.questionId) }
            val command = QuestionPaymentCommand(userId, questionOrderCommand)
            
            every { questionOrderGenerator.generateQuestionOrder(any(), any()) } returns questionOrder
            justRun { promotionApplier.apply(any()) }
            justRun { orderCouponApplier.apply(any(), any()) }
            justRun { paymentCouponApplier.apply(any(), any()) }
            justRun { pointCommandAPI.usePoint(any(), any()) }
            justRun { questionPaymentRecorder.record(any()) }
            justRun { eventPublisher.publish(any()) }
            
            When("사용자가 문제 결제를 요청하면") {
                questionPaymentService.payment(command)
                Then("결제가 완료되고 이벤트가 발행된다") {
                    verify(exactly(1)) { eventPublisher.publish(any()) }
                }
            }
        }
        
        Given("사용자가 포인트가 부족할 때") {
            val userId = 1L
            val questionOrder = QuestionOrderScenario.create(5)
            val questionOrderCommand = questionOrder.items.map { QuestionOrderCommand(it.questionInfo.questionId) }
            val command = QuestionPaymentCommand(userId, questionOrderCommand)
            
            every { questionOrderGenerator.generateQuestionOrder(any(), any()) } returns questionOrder
            justRun { promotionApplier.apply(any()) }
            justRun { orderCouponApplier.apply(any(), any()) }
            justRun { paymentCouponApplier.apply(any(), any()) }
            every { pointCommandAPI.usePoint(any(), any()) } throws CoreException(Error.NOT_ENOUGH_POINT)
            justRun { questionPaymentRecorder.record(any()) }
            justRun { eventPublisher.publish(any()) }
            
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