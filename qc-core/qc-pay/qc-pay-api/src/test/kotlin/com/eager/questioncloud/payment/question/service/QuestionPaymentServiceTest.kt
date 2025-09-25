package com.eager.questioncloud.payment.question.service

import com.eager.questioncloud.common.event.EventPublisher
import com.eager.questioncloud.payment.question.command.QuestionPaymentCommand
import com.eager.questioncloud.payment.question.implement.QuestionOrderGenerator
import com.eager.questioncloud.payment.question.implement.QuestionPaymentCouponReader
import com.eager.questioncloud.payment.question.implement.QuestionPaymentProcessor
import com.eager.questioncloud.payment.scenario.QuestionPaymentScenario
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.ints.exactly
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
    private lateinit var questionPaymentProcessor: QuestionPaymentProcessor
    
    @MockkBean
    private lateinit var questionOrderGenerator: QuestionOrderGenerator
    
    @MockkBean
    private lateinit var questionPaymentCouponReader: QuestionPaymentCouponReader
    
    @MockkBean
    private lateinit var eventPublisher: EventPublisher
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("사용자가 문제를 결제할 때") {
            val userId = 1L
            val questionPaymentScenario = QuestionPaymentScenario.create(userId, 10)
            val command = QuestionPaymentCommand(userId, questionPaymentScenario.questionPayment.order.questionIds, null)
            
            every { questionOrderGenerator.generateQuestionOrder(any(), any()) } returns questionPaymentScenario.questionPayment.order
            
            every {
                questionPaymentCouponReader.getQuestionPaymentCoupon(
                    any(),
                    any()
                )
            } returns questionPaymentScenario.questionPayment.questionPaymentCoupon
            
            justRun { questionPaymentProcessor.payment(any()) }
            
            justRun { eventPublisher.publish(any()) }
            
            When("결제를 요청하면") {
                questionPaymentService.payment(command)
                
                Then("결제가 완료되고 이벤트가 발행된다") {
                    verify(exactly(1)) { eventPublisher.publish(any()) }
                }
            }
        }
    }
}