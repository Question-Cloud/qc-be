package com.eager.questioncloud.payment.coupon.service

import com.eager.questioncloud.payment.domain.Coupon
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import com.eager.questioncloud.payment.scenario.CouponScenario
import com.eager.questioncloud.payment.scenario.custom
import com.eager.questioncloud.payment.scenario.save
import com.eager.questioncloud.payment.scenario.setUserCoupon
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class UserCouponServiceTest(
    private val userCouponService: UserCouponService,
    private val userCouponRepository: UserCouponRepository,
    private val couponRepository: CouponRepository,
    private val dbCleaner: DBCleaner
) : BehaviorSpec() {
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("사용자가 여러 쿠폰을 보유하고 있을 때") {
            val userId = 1L
            
            val availableCouponCount = 10
            val unavailableCouponCount = 5
            
            (1..availableCouponCount).forEach { _ ->
                val coupon = CouponScenario.paymentFixedCoupon().save(couponRepository)
                coupon.setUserCoupon(userId, userCouponRepository)
            }
            
            (1..unavailableCouponCount).forEach { _ ->
                val coupon = CouponScenario.paymentFixedCoupon().custom { set(Coupon::endAt, LocalDateTime.now().minusDays(10)) }
                    .save(couponRepository)
                coupon.setUserCoupon(userId, userCouponRepository)
            }
            
            When("사용 가능한 쿠폰 목록을 조회하면") {
                val availableCoupons = userCouponService.getAvailableUserCoupons(userId)
                
                Then("사용 가능한 쿠폰들이 조회된다.") {
                    availableCoupons shouldHaveSize availableCouponCount
                }
            }
        }
        
        Given("사용자가 사용 가능한 쿠폰을 보유하고 있지 않을 때") {
            val userId = 1L
            val couponCount = 5
            (1..couponCount).forEach { _ ->
                val coupon = CouponScenario.paymentFixedCoupon().custom { set(Coupon::endAt, LocalDateTime.now().minusDays(10)) }
                    .save(couponRepository)
                coupon.setUserCoupon(userId, userCouponRepository)
            }
            
            (1..couponCount).forEach { _ ->
                val coupon = CouponScenario.paymentFixedCoupon().save(couponRepository)
                coupon.setUserCoupon(userId, userCouponRepository, true)
            }
            
            When("사용 가능한 쿠폰 목록을 조회하면") {
                val availableCoupons = userCouponService.getAvailableUserCoupons(userId)
                
                Then("빈 목록이 반환된다") {
                    availableCoupons.shouldBeEmpty()
                }
            }
        }
        
        Given("동일한 사용자가 쿠폰 등록 요청을 동시에 할 때") {
            val userId = 1L
            val coupon = CouponScenario.paymentFixedCoupon().save(couponRepository)
            
            When("여러번 등록 요청을 하면") {
                val threadCount = 100
                val executorService = Executors.newFixedThreadPool(threadCount)
                val latch = CountDownLatch(threadCount)
                val successCount = AtomicInteger(0)
                val failureCount = AtomicInteger(0)
                
                repeat(threadCount) {
                    executorService.submit {
                        runCatching {
                            userCouponService.registerCoupon(userId, coupon.code)
                        }.onSuccess {
                            successCount.incrementAndGet()
                        }.onFailure {
                            failureCount.incrementAndGet()
                        }.also {
                            latch.countDown()
                        }.getOrNull()
                    }
                }
                
                latch.await(10, TimeUnit.SECONDS)
                executorService.shutdown()
                executorService.awaitTermination(5, TimeUnit.SECONDS)
                
                Then("1회만 성공하고 나머지는 모두 실패한다") {
                    successCount.get() shouldBe 1
                    failureCount.get() shouldBe 99
                    
                    val isRegistered = userCouponRepository.isRegistered(userId, coupon.id)
                    isRegistered shouldBe true
                }
            }
        }
        
        Given("제한된 수량의 쿠폰이 있을 때") {
            val coupon = CouponScenario.paymentFixedCoupon().custom { set(Coupon::remainingCount, 10) }.save(couponRepository)
            val couponLimit = coupon.remainingCount
            val requestCount = 100
            
            When("서로 다른 사용자 100명이 동시에 등록 요청을 하면") {
                val executorService = Executors.newFixedThreadPool(requestCount)
                val latch = CountDownLatch(requestCount)
                val successCount = AtomicInteger(0)
                val limitedCouponFailureCount = AtomicInteger(0)
                
                for (userId in 1L..100L) {
                    executorService.submit {
                        runCatching {
                            userCouponService.registerCoupon(userId, coupon.code)
                        }.onSuccess {
                            successCount.incrementAndGet()
                        }.onFailure {
                            limitedCouponFailureCount.incrementAndGet()
                        }.also {
                            latch.countDown()
                        }.getOrNull()
                    }
                }
                
                latch.await(10, TimeUnit.SECONDS)
                executorService.shutdown()
                executorService.awaitTermination(5, TimeUnit.SECONDS)
                
                Then("쿠폰 수량만큼만 발급되고 나머지는 실패한다") {
                    successCount.get() shouldBe couponLimit
                    limitedCouponFailureCount.get() shouldBe (requestCount - couponLimit)
                    
                    val updatedCoupon = couponRepository.findById(coupon.id)
                    updatedCoupon.remainingCount shouldBe 0
                    
                    val registeredCount = (1L..100L).count { userId ->
                        userCouponRepository.isRegistered(userId, coupon.id)
                    }
                    
                    registeredCount shouldBe couponLimit
                }
            }
        }
    }
}