package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.domain.QuestionOrderItem
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import com.eager.questioncloud.payment.scenario.CouponScenario
import com.eager.questioncloud.payment.scenario.setUserCoupon
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*
import kotlin.math.floor

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class QuestionPaymentCouponProcessorTest(
    private val userCouponRepository: UserCouponRepository,
    private val couponRepository: CouponRepository,
    private val questionPaymentCouponProcessor: QuestionPaymentCouponProcessor,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    private fun createQuestionPayment(
        userId: Long,
        questionPaymentCoupon: QuestionPaymentCoupon?,
        orderItems: List<QuestionOrderItem> = listOf(
            QuestionOrderItem(questionId = 1L, price = 1000),
            QuestionOrderItem(questionId = 2L, price = 1000),
            QuestionOrderItem(questionId = 3L, price = 1000)
        )
    ): QuestionPayment {
        val questionOrder = QuestionOrder(UUID.randomUUID().toString(), orderItems)
        return QuestionPayment.create(userId, questionPaymentCoupon, questionOrder)
    }
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("사용자가 고정 할인 쿠폰을 보유하고 있을 때") {
            val userId = 1L
            val coupon = CouponScenario.available(1, couponRepository).coupons[0]
            val userCoupon = coupon.setUserCoupon(userId, userCouponRepository)
            
            val questionPaymentCoupon = QuestionPaymentCoupon.create(userCoupon.id, coupon)
            val questionPayment = createQuestionPayment(userId, questionPaymentCoupon)
            val originalAmount = questionPayment.amount
            
            When("쿠폰을 적용하면") {
                questionPaymentCouponProcessor.applyCoupon(questionPayment)
                
                Then("설정된 고정 금액만큼 할인된다") {
                    val afterAmount = questionPayment.amount
                    val actualDiscountAmount = originalAmount - afterAmount
                    
                    actualDiscountAmount shouldBe coupon.value
                }
            }
        }
        
        Given("사용자가 비율 할인 쿠폰을 보유하고 있을 때") {
            val userId = 1L
            val coupon = CouponScenario.available(1, couponRepository, CouponType.Percent).coupons[0]
            val userCoupon = coupon.setUserCoupon(userId, userCouponRepository)
            val questionPaymentCoupon = QuestionPaymentCoupon.create(userCoupon.id, coupon)
            val questionPayment = createQuestionPayment(userId, questionPaymentCoupon)
            val originalAmount = questionPayment.amount
            
            When("쿠폰을 적용하면") {
                questionPaymentCouponProcessor.applyCoupon(questionPayment)
                
                Then("비율에 따른 할인 금액이 적용된다 (소수점 버림)") {
                    val afterAmount = questionPayment.amount
                    val actualDiscountAmount = originalAmount - afterAmount
                    
                    val expectedDiscountAmount = floor(originalAmount.toDouble() * (coupon.value.toDouble() / 100.0)).toInt()
                    
                    actualDiscountAmount shouldBe expectedDiscountAmount
                }
            }
        }
    }
}