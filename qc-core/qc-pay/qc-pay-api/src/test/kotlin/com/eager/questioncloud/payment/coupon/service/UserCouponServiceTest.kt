package com.eager.questioncloud.payment.coupon.service

import com.eager.questioncloud.payment.coupon.implement.UserCouponRegister
import com.eager.questioncloud.payment.dto.AvailableUserCoupon
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.enums.DiscountCalculationType
import com.eager.questioncloud.payment.repository.UserCouponRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.time.LocalDateTime

class UserCouponServiceTest : BehaviorSpec() {
    private val userCouponRegister = mockk<UserCouponRegister>()
    private val userCouponRepository = mockk<UserCouponRepository>()
    
    private val userCouponService = UserCouponService(
        userCouponRegister,
        userCouponRepository
    )
    
    init {
        afterEach {
            clearMocks(userCouponRegister, userCouponRepository)
        }
        
        Given("쿠폰 등록") {
            val userId = 1L
            val couponCode = "TEST-COUPON-CODE"
            
            justRun { userCouponRegister.registerCoupon(userId, couponCode) }
            
            When("사용자가 쿠폰을 등록하면") {
                userCouponService.registerCoupon(userId, couponCode)
                
                Then("쿠폰이 등록된다") {
                    verify(exactly = 1) { userCouponRegister.registerCoupon(userId, couponCode) }
                }
            }
        }
        
        Given("사용 가능한 쿠폰 목록 조회") {
            val userId = 1L
            
            val availableCoupons = listOf(
                AvailableUserCoupon(
                    id = 1L,
                    title = "10% 할인 쿠폰",
                    couponType = CouponType.PAYMENT,
                    discountCalculationType = DiscountCalculationType.PERCENT,
                    minimumPurchaseAmount = 10000,
                    maximumDiscountAmount = 5000,
                    isDuplicable = false,
                    value = 10,
                    endAt = LocalDateTime.now().plusDays(7)
                ),
                AvailableUserCoupon(
                    id = 2L,
                    title = "5000원 할인 쿠폰",
                    couponType = CouponType.PAYMENT,
                    discountCalculationType = DiscountCalculationType.FIXED,
                    minimumPurchaseAmount = 20000,
                    maximumDiscountAmount = 5000,
                    isDuplicable = false,
                    value = 5000,
                    endAt = LocalDateTime.now().plusDays(14)
                )
            )
            
            every { userCouponRepository.getAvailableUserCoupons(userId) } returns availableCoupons
            
            When("사용 가능한 쿠폰 목록을 조회하면") {
                val result = userCouponService.getAvailableUserCoupons(userId)
                
                Then("사용 가능한 쿠폰 목록이 반환된다") {
                    result shouldHaveSize 2
                    result[0].title shouldBe availableCoupons[0].title
                    result[1].title shouldBe availableCoupons[1].title
                    
                    verify(exactly = 1) { userCouponRepository.getAvailableUserCoupons(userId) }
                }
            }
        }
        
        Given("사용 가능한 쿠폰이 없는 경우") {
            val userId = 1L
            
            every { userCouponRepository.getAvailableUserCoupons(userId) } returns emptyList()
            
            When("사용 가능한 쿠폰 목록을 조회하면") {
                val result = userCouponService.getAvailableUserCoupons(userId)
                
                Then("빈 목록이 반환된다") {
                    result.shouldBeEmpty()
                    
                    verify(exactly = 1) { userCouponRepository.getAvailableUserCoupons(userId) }
                }
            }
        }
    }
}