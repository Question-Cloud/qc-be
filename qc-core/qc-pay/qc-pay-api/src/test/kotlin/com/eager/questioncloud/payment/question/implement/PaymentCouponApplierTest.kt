package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.question.command.QuestionOrderCommand
import com.eager.questioncloud.payment.question.command.QuestionPaymentCommand
import com.eager.questioncloud.payment.repository.CouponInformationRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import com.eager.questioncloud.payment.scenario.CouponScenario
import com.eager.questioncloud.payment.scenario.QuestionOrderScenario
import com.eager.questioncloud.payment.scenario.save
import com.eager.questioncloud.payment.scenario.setUserCoupon
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
class PaymentCouponApplierTest(
    private val paymentCouponApplier: PaymentCouponApplier,
    private val couponInformationRepository: CouponInformationRepository,
    private val userCouponRepository: UserCouponRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("고정 금액 결제 할인 쿠폰을 사용하는 주문이 있을 때") {
            val userId = 1L
            val questionOrder = QuestionOrderScenario.create(3)
            val questionOrderCommands = questionOrder.items.map { QuestionOrderCommand(it.questionInfo.questionId) }
            
            val discountAmount = 3000
            val paymentCoupon = CouponScenario.paymentFixedCoupon(discountAmount).save(couponInformationRepository)
            val paymentUserCoupon = paymentCoupon.setUserCoupon(userId, userCouponRepository)
            
            val questionPayment = QuestionPayment.create(userId, questionOrder)
            val questionPaymentCommand = QuestionPaymentCommand(userId, questionOrderCommands, paymentUserCoupon.id)
            
            When("결제 할인 쿠폰을 적용하면") {
                paymentCouponApplier.apply(questionPayment, questionPaymentCommand)
                Then("최종 결제 금액이 할인되고 쿠폰은 사용처리 된다.") {
                    questionPayment.appliedPaymentCoupons.size shouldBe 1
                    questionPayment.realAmount shouldBe questionPayment.originalAmount - discountAmount
                    val usedUserCoupon = userCouponRepository.getUserCoupon(paymentUserCoupon.id)
                    usedUserCoupon.isUsed shouldBe true
                }
            }
        }
        
        Given("퍼센트 결제 할인 쿠폰을 사용하는 주문이 있을 때") {
            val userId = 1L
            val questionOrder = QuestionOrderScenario.create(3)
            val questionOrderCommands = questionOrder.items.map { QuestionOrderCommand(it.questionInfo.questionId) }
            
            val discountPercent = 30
            val paymentCoupon =
                CouponScenario.paymentPercentCoupon(discountPercent, maxDiscount = Int.MAX_VALUE).save(couponInformationRepository)
            val paymentUserCoupon = paymentCoupon.setUserCoupon(userId, userCouponRepository)
            
            val questionPayment = QuestionPayment.create(userId, questionOrder)
            val questionPaymentCommand = QuestionPaymentCommand(userId, questionOrderCommands, paymentUserCoupon.id)
            
            When("결제 할인 쿠폰을 적용하면") {
                paymentCouponApplier.apply(questionPayment, questionPaymentCommand)
                Then("최종 결제 금액이 할인되고 쿠폰은 사용처리 된다.") {
                    questionPayment.appliedPaymentCoupons.size shouldBe 1
                    val expectedDiscountAmount = questionPayment.originalAmount * discountPercent / 100
                    questionPayment.realAmount shouldBe questionPayment.originalAmount - expectedDiscountAmount
                    val usedUserCoupon = userCouponRepository.getUserCoupon(paymentUserCoupon.id)
                    usedUserCoupon.isUsed shouldBe true
                }
            }
        }
    }
}
