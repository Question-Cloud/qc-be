package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import com.eager.questioncloud.payment.scenario.CouponScenario
import com.eager.questioncloud.payment.scenario.setUserCoupon
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class QuestionPaymentCouponReaderTest(
    private val userCouponRepository: UserCouponRepository,
    private val couponRepository: CouponRepository,
    private val questionPaymentCouponReader: QuestionPaymentCouponReader,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("사용자가 유효한 쿠폰을 보유하고 있을 때") {
            val userId = 1L
            val coupon = CouponScenario.available(1, couponRepository).coupons[0]
            val userCoupon = coupon.setUserCoupon(userId, userCouponRepository)
            
            When("쿠폰을 사용하기 위해 조회하면") {
                val questionPaymentCoupon = questionPaymentCouponReader.getQuestionPaymentCoupon(
                    userCoupon.id,
                    userId
                )
                
                Then("쿠폰 정보가 올바르게 반환된다") {
                    questionPaymentCoupon shouldNotBe null
                    questionPaymentCoupon!!.userCouponId shouldBe userCoupon.id
                    questionPaymentCoupon.title shouldBe coupon.title
                    questionPaymentCoupon.couponType shouldBe coupon.couponType
                    questionPaymentCoupon.value shouldBe coupon.value
                }
            }
        }
        
        Given("사용자가 만료된 쿠폰을 보유하고 있을 때") {
            val userId = 1L
            val expiredCoupon = CouponScenario.expired(1, couponRepository).coupons[0]
            val userCoupon = expiredCoupon.setUserCoupon(userId, userCouponRepository)
            
            When("쿠폰을 사용하기 위해 조회하려고 하면") {
                Then("EXPIRED_COUPON 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        questionPaymentCouponReader.getQuestionPaymentCoupon(userCoupon.id, userId)
                    }
                    exception.error shouldBe Error.EXPIRED_COUPON
                }
            }
        }
        
        Given("사용자가 이미 사용한 쿠폰을 보유하고 있을 때") {
            val userId = 1L
            val coupon = CouponScenario.available(1, couponRepository).coupons[0]
            val usedUserCoupon = coupon.setUserCoupon(userId, userCouponRepository, isUsed = true)
            
            When("쿠폰을 사용하기 위해 조회하려고 하면") {
                Then("WRONG_COUPON 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        questionPaymentCouponReader.getQuestionPaymentCoupon(usedUserCoupon.id, userId)
                    }
                    exception.error shouldBe Error.WRONG_COUPON
                }
            }
        }
        
        Given("쿠폰을 사용하지 않는 경우") {
            val userId = 1L
            val userCouponId: Long? = null
            
            When("쿠폰 검증 조회 시") {
                val questionPaymentCoupon = questionPaymentCouponReader.getQuestionPaymentCoupon(
                    userCouponId,
                    userId
                )
                
                Then("null이 반환된다") {
                    questionPaymentCoupon shouldBe null
                }
            }
        }
    }
}