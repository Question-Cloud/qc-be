package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.FixedCoupon
import com.eager.questioncloud.payment.domain.PercentCoupon
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.question.command.QuestionPaymentCommand
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import com.eager.questioncloud.payment.scenario.CouponScenario
import com.eager.questioncloud.payment.scenario.QuestionPaymentScenario
import com.eager.questioncloud.payment.scenario.save
import com.eager.questioncloud.payment.scenario.setUserCoupon
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class CouponPolicyApplierTest(
    private val couponPolicyApplier: CouponPolicyApplier,
    private val userCouponRepository: UserCouponRepository,
    private val couponRepository: CouponRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("쿠폰을 사용하지 않을 때") {
            val userId = 1L
            val questionPaymentScenario = QuestionPaymentScenario.create(10)
            val questionPayment = QuestionPayment.create(userId, questionPaymentScenario.order)
            val questionPaymentCommand = QuestionPaymentCommand(userId, questionPaymentScenario.order.questionIds, null)
            When("할인 정책을 적용하면") {
                couponPolicyApplier.apply(questionPayment, questionPaymentCommand)
                Then("기본 정책이 적용된다.") {
                    questionPayment.appliedDiscountList.size shouldBeEqual 0
                    questionPayment.realAmount shouldBeEqual questionPayment.originalAmount
                }
            }
        }
        
        Given("고정 할인 쿠폰을 사용할 때") {
            val userId = 1L
            val coupon = CouponScenario.available(CouponType.Fixed).coupon.save(couponRepository)
            val userCoupon = coupon.setUserCoupon(userId, userCouponRepository)
            val questionPaymentScenario = QuestionPaymentScenario.create(10)
            val questionPayment = QuestionPayment.create(userId, questionPaymentScenario.order)
            val questionPaymentCommand = QuestionPaymentCommand(userId, questionPaymentScenario.order.questionIds, userCoupon.id)
            When("할인 정책을 적용하면") {
                couponPolicyApplier.apply(questionPayment, questionPaymentCommand)
                Then("고정 할인 쿠폰 정책이 적용되고 사용처리 된다.") {
                    questionPayment.appliedDiscountList.size shouldBeEqual 1
                    questionPayment.appliedDiscountList[0].shouldBeTypeOf<FixedCoupon>()
                    questionPayment.realAmount shouldBeEqual questionPayment.originalAmount - questionPayment.appliedDiscountList[0].getDiscountAmount(
                        questionPayment.originalAmount
                    )
                    
                    val usedUserCoupon = userCouponRepository.getUserCoupon(userCoupon.id)
                    usedUserCoupon.isUsed shouldBe true
                    usedUserCoupon.usedOrderId shouldBe questionPayment.order.orderId
                }
            }
        }
        
        Given("퍼센트 할인 쿠폰을 사용할 때") {
            val userId = 1L
            val coupon = CouponScenario.available(CouponType.Percent).coupon.save(couponRepository)
            val userCoupon = coupon.setUserCoupon(userId, userCouponRepository)
            val questionPaymentScenario = QuestionPaymentScenario.create(10)
            val questionPayment = QuestionPayment.create(userId, questionPaymentScenario.order)
            val questionPaymentCommand = QuestionPaymentCommand(userId, questionPaymentScenario.order.questionIds, userCoupon.id)
            When("할인 정책을 적용하면") {
                couponPolicyApplier.apply(questionPayment, questionPaymentCommand)
                Then("퍼센트 할인 쿠폰 정책이 적용되고 사용처리 된다.") {
                    questionPayment.appliedDiscountList.size shouldBeEqual 1
                    questionPayment.appliedDiscountList[0].shouldBeTypeOf<PercentCoupon>()
                    questionPayment.realAmount shouldBeEqual questionPayment.originalAmount - questionPayment.appliedDiscountList[0].getDiscountAmount(
                        questionPayment.originalAmount
                    )
                    
                    val usedUserCoupon = userCouponRepository.getUserCoupon(userCoupon.id)
                    usedUserCoupon.isUsed shouldBe true
                    usedUserCoupon.usedOrderId shouldBe questionPayment.order.orderId
                }
            }
        }
        
        Given("이미 사용한 쿠폰을 사용할 때") {
            val userId = 1L
            val coupon = CouponScenario.available(CouponType.Percent).coupon.save(couponRepository)
            val userCoupon = coupon.setUserCoupon(userId, userCouponRepository, isUsed = true)
            val questionPaymentScenario = QuestionPaymentScenario.create(10)
            val questionPayment = QuestionPayment.create(userId, questionPaymentScenario.order)
            val questionPaymentCommand = QuestionPaymentCommand(userId, questionPaymentScenario.order.questionIds, userCoupon.id)
            When("할인 정책을 적용하면") {
                Then("WRONG_COUPON 예외가 발생한다.") {
                    val exception = shouldThrow<CoreException> {
                        couponPolicyApplier.apply(questionPayment, questionPaymentCommand)
                    }
                    exception.error shouldBe Error.WRONG_COUPON
                }
            }
        }
        
        Given("존재하지 않는 쿠폰을 사용할 때") {
            val userId = 1L
            val wrongUserCouponId = 1L
            val questionPaymentScenario = QuestionPaymentScenario.create(10)
            val questionPayment = QuestionPayment.create(userId, questionPaymentScenario.order)
            val questionPaymentCommand = QuestionPaymentCommand(userId, questionPaymentScenario.order.questionIds, wrongUserCouponId)
            When("할인 정책을 적용하면") {
                Then("WRONG_COUPON 예외가 발생한다.") {
                    val exception = shouldThrow<CoreException> {
                        couponPolicyApplier.apply(questionPayment, questionPaymentCommand)
                    }
                    exception.error shouldBe Error.WRONG_COUPON
                }
            }
        }
        
        Given("동일한 쿠폰을 여러 결제에 동시에 사용할 때") {
            val userId = 1L
            val coupon = CouponScenario.available(CouponType.Fixed).coupon.save(couponRepository)
            val userCoupon = coupon.setUserCoupon(userId, userCouponRepository)
            val questionPaymentScenarios = (1..100).map { QuestionPaymentScenario.create(5) }
            val questionPayments = questionPaymentScenarios.map { QuestionPayment.create(userId, it.order) }
            val questionPaymentCommands =
                questionPayments.map { QuestionPaymentCommand(userId, it.order.questionIds, userCoupon.id) }
            
            val successCount = AtomicInteger(0)
            val failCount = AtomicInteger(0)
            val latch = CountDownLatch(questionPaymentScenarios.size)
            
            When("할인 정책을 적용하면") {
                val excutors = Executors.newFixedThreadPool(questionPaymentScenarios.size)
                for (i in 1 until questionPaymentScenarios.size) {
                    excutors.submit {
                        try {
                            couponPolicyApplier.apply(questionPayments[i], questionPaymentCommands[i])
                            successCount.getAndIncrement()
                        } catch (e: Exception) {
                            failCount.incrementAndGet()
                        } finally {
                            latch.countDown()
                        }
                    }
                }
                latch.await(10, TimeUnit.SECONDS)
                excutors.shutdown()
                Then("1개의 결제만 성공한다.") {
                    successCount.get() shouldBe 1
                }
            }
        }
    }
}
