package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.domain.Coupon
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
    private val couponInformationRepository: CouponInformationRepository,
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
            val questionPayment = QuestionPayment.create(userId, questionOrder)
            val questionId = questionOrder.items[0].questionInfo.questionId
            
            // 프로모션
            val promotionSalePrice = 7000
            val promotion = PromotionScenario.create(questionId, promotionSalePrice)
            promotionRepository.save(promotion)
            questionOrder.items[0].applyPromotion(promotion)
            
            // 쿠폰 + 중복 쿠폰
            val couponDiscountAmount = 1000
            
            val couponInformation =
                CouponScenario.productFixedCoupon(questionId, discount = couponDiscountAmount).save(couponInformationRepository)
            val userCoupon = couponInformation.setUserCoupon(userId, userCouponRepository)
            val coupon = Coupon.createProductCoupon(questionId, couponInformation, userCoupon)
            
            val duplicableCouponInformation =
                CouponScenario.duplicableFixedProductCoupon(questionId, discount = couponDiscountAmount).save(couponInformationRepository)
            val duplicabeUserCoupon = duplicableCouponInformation.setUserCoupon(userId, userCouponRepository)
            val duplicableCoupon = Coupon.createDuplicableProductCoupon(questionId, duplicableCouponInformation, duplicabeUserCoupon)
            
            // 결제 할인 쿠폰
            val paymentCouponInformation =
                CouponScenario.paymentFixedCoupon(discount = couponDiscountAmount).save(couponInformationRepository)
            val paymentUserCoupon = paymentCouponInformation.setUserCoupon(userId, userCouponRepository)
            val paymentCoupon = Coupon.createPaymentCoupon(paymentCouponInformation, paymentUserCoupon)
            
            coupon.apply(questionPayment)
            duplicableCoupon.apply(questionPayment)
            paymentCoupon.apply(questionPayment)
            
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
                        couponInformation.title,
                        duplicableCouponInformation.title,
                        paymentCoupon.couponInformation.title,
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
