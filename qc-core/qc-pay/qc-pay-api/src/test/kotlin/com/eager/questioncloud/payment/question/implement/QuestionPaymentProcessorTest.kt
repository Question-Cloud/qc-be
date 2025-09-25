package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.domain.QuestionOrderItem
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.QuestionPaymentRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import com.eager.questioncloud.payment.scenario.CouponScenario
import com.eager.questioncloud.payment.scenario.setUserCoupon
import com.eager.questioncloud.point.api.internal.PointCommandAPI
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class QuestionPaymentProcessorTest(
    private val questionPaymentProcessor: QuestionPaymentProcessor,
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository,
    private val questionPaymentRepository: QuestionPaymentRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var pointCommandAPI: PointCommandAPI
    
    private fun createDefaultOrderItems(): List<QuestionOrderItem> {
        return listOf(
            QuestionOrderItem(questionId = 1L, price = 1000),
            QuestionOrderItem(questionId = 2L, price = 1000),
            QuestionOrderItem(questionId = 3L, price = 1000)
        )
    }
    
    private fun createQuestionPayment(
        userId: Long,
        questionPaymentCoupon: QuestionPaymentCoupon? = null,
        orderItems: List<QuestionOrderItem> = createDefaultOrderItems()
    ): QuestionPayment {
        val questionOrder = QuestionOrder(UUID.randomUUID().toString(), orderItems)
        return QuestionPayment.create(userId, questionPaymentCoupon, questionOrder)
    }
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("사용자가 할인 쿠폰을 사용하여 문제를 결제할 때") {
            val userId = 1L
            
            val coupon = CouponScenario.available(1, couponRepository).coupons[0]
            val userCoupon = coupon.setUserCoupon(userId, userCouponRepository)
            val questionPaymentCoupon = QuestionPaymentCoupon.create(userCoupon.id, coupon)
            val questionPayment = createQuestionPayment(userId, questionPaymentCoupon)
            
            val originalAmount = questionPayment.amount
            val discountedAmount = questionPayment.questionPaymentCoupon!!.calcDiscount(originalAmount)
            
            justRun { pointCommandAPI.usePoint(any(), any()) }
            
            When("결제 처리를 요청하면") {
                questionPaymentProcessor.payment(questionPayment)
                
                Then("쿠폰이 사용됨으로 처리되고 할인된 금액으로 결제가 완료된다") {
                    val afterUserCoupon = userCouponRepository.getUserCoupon(userCoupon.id)
                    afterUserCoupon.isUsed shouldBe true
                    
                    questionPayment.amount shouldBe discountedAmount
                    
                    val paymentCount = questionPaymentRepository.countByUserId(userId)
                    paymentCount shouldBe 1
                }
            }
        }
        
        Given("사용자가 쿠폰 없이 문제를 결제할 때") {
            val userId = 1L
            val questionPayment = createQuestionPayment(userId, null)
            
            justRun { pointCommandAPI.usePoint(any(), any()) }
            
            When("결제 처리를 요청하면") {
                questionPaymentProcessor.payment(questionPayment)
                
                Then("정가로 결제가 완료된다") {
                    val paymentCount = questionPaymentRepository.countByUserId(userId)
                    paymentCount shouldBe 1
                }
            }
        }
        
        Given("사용자의 보유 포인트가 부족할 때") {
            val userId = 1L
            val questionPayment = createQuestionPayment(userId, null)
            
            every { pointCommandAPI.usePoint(any(), any()) } throws CoreException(Error.NOT_ENOUGH_POINT)
            
            When("결제 처리를 요청하면") {
                Then("NOT_ENOUGH_POINT 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        questionPaymentProcessor.payment(questionPayment)
                    }
                    exception.error shouldBe Error.NOT_ENOUGH_POINT
                }
            }
        }
    }
}