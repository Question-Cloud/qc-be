package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.question.command.QuestionOrderCommand
import com.eager.questioncloud.payment.question.command.QuestionPaymentCommand
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import com.eager.questioncloud.payment.scenario.CouponScenario
import com.eager.questioncloud.payment.scenario.QuestionOrderScenario
import com.eager.questioncloud.payment.scenario.save
import com.eager.questioncloud.payment.scenario.setUserCoupon
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class OrderCouponApplierTest(
    private val orderCouponApplier: OrderCouponApplier,
    private val userCouponRepository: UserCouponRepository,
    private val couponRepository: CouponRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("상품 쿠폰을 적용 해야하는 주문이 있을 때") {
            val userId = 1L
            val questionOrder = QuestionOrderScenario.create(3)
            
            // 단일 고정 할인 상품 쿠폰
            val item0 = questionOrder.items[0]
            val item0DiscountAmount = 1000
            val item0Coupon = CouponScenario.productFixedCoupon(item0.questionInfo.questionId, item0DiscountAmount)
                .save(couponRepository)
            val item0UserCoupon = item0Coupon.setUserCoupon(userId, userCouponRepository)
            
            // 단일 고정 할인 상품 쿠폰 + 중복 쿠폰
            val item1 = questionOrder.items[1]
            val item1DiscountAmount = 1000
            val item1Coupon = CouponScenario.productFixedCoupon(item1.questionInfo.questionId, item1DiscountAmount)
                .save(couponRepository)
            val item1DuplicableCoupon = CouponScenario.duplicableFixedProductCoupon(item1.questionInfo.questionId, item1DiscountAmount)
                .save(couponRepository)
            val item1UserCoupon = item1Coupon.setUserCoupon(userId, userCouponRepository)
            val item1DuplicableUserCoupon = item1DuplicableCoupon.setUserCoupon(userId, userCouponRepository)
            
            // 쿠폰 사용 X
            val item2 = questionOrder.items[2]
            
            val questionOrderCommands = listOf(
                QuestionOrderCommand(item0.questionInfo.questionId, item0UserCoupon.id, null),
                QuestionOrderCommand(item1.questionInfo.questionId, item1UserCoupon.id, item1DuplicableUserCoupon.id),
                QuestionOrderCommand(item2.questionInfo.questionId, null, null),
            )
            
            val questionPaymentCommand = QuestionPaymentCommand(userId, questionOrderCommands)
            
            When("쿠폰을 적용하면") {
                orderCouponApplier.apply(questionOrder, questionPaymentCommand)
                Then("쿠폰이 적용된 상품은 할인된 가격으로 변경되고 쿠폰은 사용 처리 된다.") {
                    val orderItem0 = questionOrder.items[0]
                    orderItem0.orderDiscountHistories.size shouldBe 1
                    orderItem0.realPrice shouldBe orderItem0.originalPrice - item1DiscountAmount
                    
                    val orderItem1 = questionOrder.items[1]
                    orderItem1.orderDiscountHistories.size shouldBe 2
                    orderItem1.realPrice shouldBe orderItem1.originalPrice - (item1DiscountAmount * 2)
                    
                    val orderItem2 = questionOrder.items[2]
                    orderItem2.orderDiscountHistories.size shouldBe 0
                    orderItem2.realPrice shouldBe orderItem2.originalPrice
                    
                    val usedItem0UserCoupon = userCouponRepository.getUserCoupon(item0UserCoupon.id)
                    usedItem0UserCoupon.isUsed shouldBe true
                    
                    val usedItem1UserCoupon = userCouponRepository.getUserCoupon(item1UserCoupon.id)
                    usedItem1UserCoupon.isUsed shouldBe true
                    
                    val usedItem1DuplicableUserCoupon = userCouponRepository.getUserCoupon(item1DuplicableUserCoupon.id)
                    usedItem1DuplicableUserCoupon.isUsed shouldBe true
                }
            }
        }
        
        Given("퍼센트 할인 쿠폰을 적용해야 하는 주문이 있을 때") {
            val userId = 1L
            val questionOrder = QuestionOrderScenario.create(1)
            
            val item = questionOrder.items[0]
            val percentValue1 = 10
            val percentValue2 = 20
            val itemPercentCoupon = CouponScenario.productPercentCoupon(item.questionInfo.questionId, percentValue1).save(couponRepository)
            val itemPercentUserCoupon = itemPercentCoupon.setUserCoupon(userId, userCouponRepository)
            
            val itemDiscountablePercentCoupon =
                CouponScenario.duplicablePercentProductCoupon(item.questionInfo.questionId, percentValue2).save(couponRepository)
            val itemDiscountablePercentUserCoupon = itemDiscountablePercentCoupon.setUserCoupon(userId, userCouponRepository)
            
            val questionOrderCommand = listOf(
                QuestionOrderCommand(item.questionInfo.questionId, itemPercentUserCoupon.id, itemDiscountablePercentUserCoupon.id),
            )
            
            val questionPaymentCommand = QuestionPaymentCommand(userId, questionOrderCommand)
            
            When("쿠폰을 적용하면") {
                orderCouponApplier.apply(questionOrder, questionPaymentCommand)
                Then("퍼센트 할인이 적용된다.") {
                    val orderItem = questionOrder.items[0]
                    val discountAmount = orderItem.originalPrice * (percentValue1 + percentValue2) / 100
                    orderItem.orderDiscountHistories.size shouldBe 2
                    orderItem.realPrice shouldBe orderItem.originalPrice - discountAmount
                }
            }
        }
        
        Given("퍼센트 할인 쿠폰과 고정 할인 쿠폰이 중복 적용해야 하는 주문이 있을 때") {
            val userId = 1L
            val questionOrder = QuestionOrderScenario.create(1)
            
            val item = questionOrder.items[0]
            val percentValue = 10
            val itemPercentCoupon = CouponScenario.productPercentCoupon(item.questionInfo.questionId, percentValue).save(couponRepository)
            val itemPercentUserCoupon = itemPercentCoupon.setUserCoupon(userId, userCouponRepository)
            
            val fixedValue = 3000
            val itemDiscountableFixedCoupon =
                CouponScenario.duplicableFixedProductCoupon(item.questionInfo.questionId, fixedValue).save(couponRepository)
            val itemDiscountableFixedUserCoupon = itemDiscountableFixedCoupon.setUserCoupon(userId, userCouponRepository)
            
            val questionOrderCommand = listOf(
                QuestionOrderCommand(item.questionInfo.questionId, itemPercentUserCoupon.id, itemDiscountableFixedUserCoupon.id),
            )
            
            val questionPaymentCommand = QuestionPaymentCommand(userId, questionOrderCommand)
            
            When("쿠폰을 적용하면") {
                orderCouponApplier.apply(questionOrder, questionPaymentCommand)
                Then("퍼센트 할인과 고정 할인이 적용된다.") {
                    val orderItem = questionOrder.items[0]
                    val discountAmount = (orderItem.originalPrice * (percentValue) / 100) + fixedValue
                    orderItem.orderDiscountHistories.size shouldBe 2
                    orderItem.realPrice shouldBe orderItem.originalPrice - discountAmount
                }
            }
        }
        
        Given("중복 사용이 불가능한 쿠폰 2개를 상품에 적용하려하는 주문이 있을 때") {
            val userId = 1L
            val questionOrder = QuestionOrderScenario.create(1)
            
            val item = questionOrder.items[0]
            
            val itemCoupon1 = CouponScenario.productFixedCoupon(item.questionInfo.questionId, 1000)
                .save(couponRepository)
            val itemUserCoupon1 = itemCoupon1.setUserCoupon(userId, userCouponRepository)
            
            val itemCoupon2 = CouponScenario.productFixedCoupon(item.questionInfo.questionId, 1500)
                .save(couponRepository)
            val itemUserCoupon2 = itemCoupon2.setUserCoupon(userId, userCouponRepository)
            
            val questionOrderCommand = listOf(QuestionOrderCommand(item.questionInfo.questionId, itemUserCoupon1.id, itemUserCoupon2.id))
            val questionPaymentCommand = QuestionPaymentCommand(userId, questionOrderCommand)
            
            When("쿠폰을 적용하면") {
                Then("예외가 발생한다.") {
                    val exception = shouldThrow<CoreException> {
                        orderCouponApplier.apply(questionOrder, questionPaymentCommand)
                    }
                    exception.error shouldBe Error.WRONG_COUPON
                }
            }
        }
        
        Given("보유하지 않은 쿠폰을 사용하려고 하는 경우") {
            val userId = 1L
            val questionOrder = QuestionOrderScenario.create(1)
            
            val wrongUserCouponId = 1L
            val wrongDuplicableUserCouponId = 2L
            
            val questionOrderCommand =
                listOf(QuestionOrderCommand(questionOrder.items[0].questionInfo.questionId, wrongUserCouponId, wrongDuplicableUserCouponId))
            val questionPaymentCommand = QuestionPaymentCommand(userId, questionOrderCommand)
            
            When("쿠폰을 적용하면") {
                Then("예외가 발생한다.") {
                    val exception = shouldThrow<CoreException> {
                        orderCouponApplier.apply(questionOrder, questionPaymentCommand)
                    }
                    exception.error shouldBe Error.WRONG_COUPON
                }
            }
        }
        
        Given("이미 사용한 쿠폰을 사용하려고 하는 경우") {
            val userId = 1L
            val questionOrder = QuestionOrderScenario.create(1)
            
            val coupon = CouponScenario.productFixedCoupon(questionOrder.items[0].questionInfo.questionId, 1500).save(couponRepository)
            val usedUserCoupon = coupon.setUserCoupon(userId, userCouponRepository, true)
            
            val questionOrderCommand =
                listOf(QuestionOrderCommand(questionOrder.items[0].questionInfo.questionId, usedUserCoupon.id))
            val questionPaymentCommand = QuestionPaymentCommand(userId, questionOrderCommand)
            
            When("쿠폰을 적용하면") {
                Then("예외가 발생한다.") {
                    val exception = shouldThrow<CoreException> {
                        orderCouponApplier.apply(questionOrder, questionPaymentCommand)
                    }
                    exception.error shouldBe Error.WRONG_COUPON
                }
            }
        }
    }
}
