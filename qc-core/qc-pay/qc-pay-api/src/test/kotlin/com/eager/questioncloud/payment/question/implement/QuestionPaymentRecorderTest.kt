package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.domain.CouponPolicy
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.repository.*
import com.eager.questioncloud.payment.scenario.*
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class QuestionPaymentRecorderTest(
    private val questionPaymentRecorder: QuestionPaymentRecorder,
    private val promotionRepository: PromotionRepository,
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository,
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
            val questionOrder = QuestionOrderScenario.create(1)
            val questionId = questionOrder.items[0].questionInfo.questionId
            
            // 프로모션
            val promotionSalePrice = 7000
            val promotion = PromotionScenario.create(questionId, promotionSalePrice)
            promotionRepository.save(promotion)
            questionOrder.items[0].applyPromotion(promotion)
            
            // 쿠폰 + 중복 쿠폰
            val couponDiscountAmount = 1000
            
            val coupon = CouponScenario.productFixedCoupon(questionId, discount = couponDiscountAmount).save(couponRepository)
            val userCoupon = coupon.setUserCoupon(userId, userCouponRepository)
            
            val duplicableCoupon =
                CouponScenario.duplicableFixedProductCoupon(questionId, discount = couponDiscountAmount).save(couponRepository)
            val duplicabeUserCoupon = duplicableCoupon.setUserCoupon(userId, userCouponRepository)
            
            questionOrder.items[0].applyCoupon(CouponPolicy(coupon, userCoupon))
            questionOrder.items[0].applyCoupon(CouponPolicy(duplicableCoupon, duplicabeUserCoupon))
            
            // 결제 할인 쿠폰
            val paymentCoupon = CouponScenario.paymentFixedCoupon(discount = couponDiscountAmount).save(couponRepository)
            val paymentUserCoupon = paymentCoupon.setUserCoupon(userId, userCouponRepository)
            
            val questionPayment = QuestionPayment.create(userId, questionOrder)
            questionPayment.applyPaymentCoupon(CouponPolicy(paymentCoupon, paymentUserCoupon))
            
            When("QuestionPayment를 기록하면") {
                questionPaymentRecorder.record(questionPayment)
                Then("주문 정보, 결제 정보, 할인 내역이 DB에 저장된다.") {
                    val questionPaymentData = questionPaymentRepository.getQuestionPaymentData(questionPayment.paymentId)
                    val questionOrderData = questionOrderRepository.getQuestionOrderData(questionPayment.orderId)
                    val discountHistories = discountHistoryRepository.findByPaymentId(questionPayment.paymentId)
                    
                    questionPaymentData.orderId shouldBe questionPayment.orderId
                    questionPaymentData.originalAmount shouldBe questionPayment.originalAmount
                    questionPaymentData.realAmount shouldBe questionPayment.realAmount
                    questionPaymentData.userId shouldBe questionPayment.userId
                    
                    questionOrderData[0].questionId shouldBe questionId
                    questionOrderData[0].originalPrice shouldBe questionOrder.items[0].originalPrice
                    questionOrderData[0].realPrice shouldBe questionOrder.items[0].realPrice
                    questionOrderData[0].promotionId shouldBe promotion.id
                    questionOrderData[0].promotionName shouldBe promotion.title
                    
                    discountHistories.map { it.name } shouldContainExactlyInAnyOrder listOf(
                        coupon.title,
                        duplicableCoupon.title,
                        paymentCoupon.title,
                    )
                    
                    discountHistories.map { it.sourceId } shouldContainExactlyInAnyOrder listOf(
                        userCoupon.id,
                        duplicabeUserCoupon.id,
                        paymentUserCoupon.id
                    )
                }
            }
        }
    }
}
