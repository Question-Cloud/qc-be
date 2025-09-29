package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.domain.FixedCoupon
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.repository.DiscountHistoryRepository
import com.eager.questioncloud.payment.repository.QuestionOrderRepository
import com.eager.questioncloud.payment.repository.QuestionPaymentRepository
import com.eager.questioncloud.payment.scenario.QuestionPaymentScenario
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class QuestionPaymentRecorderTest(
    private val questionPaymentRecorder: QuestionPaymentRecorder,
    private val questionOrderRepository: QuestionOrderRepository,
    private val questionPaymentRepository: QuestionPaymentRepository,
    private val discountHistoryRepository: DiscountHistoryRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("결제가 완료 된 QuestionPayment가 있을 때") {
            val userId = 1L
            val questionPaymentScenario = QuestionPaymentScenario.create(10)
            val questionPayment = QuestionPayment.create(userId, questionPaymentScenario.order)
            val usedCoupon = FixedCoupon(1L, 1L, "Fixed Coupon", 1000)
            questionPayment.applyDiscount(usedCoupon)
            When("QuestionPayment를 기록하면") {
                questionPaymentRecorder.record(questionPayment)
                Then("주문 정보, 결제 정보, 할인 내역이 DB에 저장된다.") {
                    val questionPaymentData = questionPaymentRepository.getQuestionPaymentData(questionPayment.order.orderId)
                    val discountHistories = discountHistoryRepository.findByOrderId(questionPayment.order.orderId)
                    val questionOrderData = questionOrderRepository.getQuestionOrderData(questionPayment.order.orderId)
                    
                    questionPaymentData.orderId shouldBe questionPayment.order.orderId
                    questionPaymentData.realAmount shouldBe questionPayment.realAmount
                    questionPaymentData.originalAmount shouldBe questionPayment.originalAmount
                    
                    discountHistories[0].name shouldBe questionPayment.discountHistory[0].name
                    discountHistories[0].discountAmount shouldBe questionPayment.discountHistory[0].discountAmount
                    
                    questionOrderData.size shouldBe questionPayment.order.items.size
                    questionOrderData.forEach { data ->
                        data.realPrice shouldBe questionPayment.order.items.find { data.questionId == it.questionId }!!.realPrice
                    }
                }
            }
        }
    }
}
