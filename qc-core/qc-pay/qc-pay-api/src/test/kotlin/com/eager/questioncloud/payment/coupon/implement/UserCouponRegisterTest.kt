package com.eager.questioncloud.payment.coupon.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import com.eager.questioncloud.payment.scenario.CouponScenario
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
class UserCouponRegisterTest(
    private val userCouponRegister: UserCouponRegister,
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository,
    private val dbCleaner: DBCleaner
) : BehaviorSpec() {
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("등록 가능한 쿠폰이 존재할 때") {
            val userId = 1L
            val coupon = CouponScenario.available().coupon.save(couponRepository)
            
            When("사용자가 쿠폰 등록 요청을 하면") {
                userCouponRegister.registerCoupon(userId, coupon.code)
                
                Then("쿠폰이 등록되고 잔여 수량이 감소한다") {
                    val isRegistered = userCouponRepository.isRegistered(userId, coupon.id)
                    isRegistered shouldBe true
                    
                    val updatedCoupon = couponRepository.findById(coupon.id)
                    updatedCoupon.remainingCount shouldBe (coupon.remainingCount - 1)
                }
            }
        }
        
        Given("사용자가 이미 등록한 쿠폰이 있을 때") {
            val userId = 1L
            val coupon = CouponScenario.available().coupon.save(couponRepository)
            coupon.setUserCoupon(userId, userCouponRepository)
            
            When("동일한 쿠폰을 다시 등록하려고 하면") {
                Then("ALREADY_REGISTER_COUPON 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        userCouponRegister.registerCoupon(userId, coupon.code)
                    }
                    exception.message shouldBe Error.ALREADY_REGISTER_COUPON.message
                }
            }
        }
        
        Given("수량이 소진된 쿠폰이 있을 때") {
            val userId = 1L
            val coupon = CouponScenario.limited().coupon.save(couponRepository)
            
            When("해당 쿠폰을 등록하려고 하면") {
                Then("LIMITED_COUPON 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        userCouponRegister.registerCoupon(userId, coupon.code)
                    }
                    exception.message shouldBe Error.LIMITED_COUPON.message
                }
            }
        }
        
        Given("유효하지 않은 쿠폰 코드로") {
            val userId = 1L
            
            When("쿠폰 등록을 시도하면") {
                Then("CoreException이 발생한다") {
                    shouldThrow<CoreException> {
                        userCouponRegister.registerCoupon(userId, "INVALID_COUPON_CODE")
                    }
                }
            }
        }
    }
}