package com.eager.questioncloud.pay.coupon.service

import com.eager.questioncloud.payment.domain.Coupon
import com.eager.questioncloud.payment.domain.UserCoupon
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.infrastructure.repository.CouponRepository
import com.eager.questioncloud.payment.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
@ActiveProfiles("test")
class UserCouponServiceTest(
    @Autowired val userCouponService: UserCouponService,
    @Autowired val userCouponRepository: UserCouponRepository,
    @Autowired val couponRepository: CouponRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `사용 가능한 쿠폰 목록을 조회할 수 있다`() {
        // given
        val userId = 1L
        
        val activeCoupon1 = couponRepository.save(
            Coupon(
                code = "ACTIVE_COUPON_1",
                title = "활성 쿠폰 1",
                couponType = CouponType.Fixed,
                value = 5000,
                remainingCount = 10,
                endAt = LocalDateTime.now().plusDays(30),
            )
        )
        
        val activeCoupon2 = couponRepository.save(
            Coupon(
                code = "ACTIVE_COUPON_2",
                title = "활성 쿠폰 2",
                couponType = CouponType.Percent,
                value = 15,
                remainingCount = 5,
                endAt = LocalDateTime.now().plusDays(15),
            )
        )
        val expiredCoupon = couponRepository.save(
            Coupon(
                code = "EXPIRED_COUPON",
                title = "만료된 쿠폰",
                couponType = CouponType.Fixed,
                value = 10000,
                remainingCount = 3,
                endAt = LocalDateTime.now().minusDays(1),
            )
        )
        
        userCouponRepository.save(UserCoupon.create(userId, activeCoupon1))
        userCouponRepository.save(UserCoupon.create(userId, activeCoupon2))
        userCouponRepository.save(
            UserCoupon(
                userId = userId,
                couponId = expiredCoupon.id,
                isUsed = false,
                createdAt = LocalDateTime.now(),
                endAt = expiredCoupon.endAt,
            )
        )
        
        // when
        val availableCoupons = userCouponService.getAvailableUserCoupons(userId)
        
        // then
        assertThat(availableCoupons).hasSize(2)
        
        val couponTitles = availableCoupons.map { it.title }
        assertThat(couponTitles).containsExactlyInAnyOrder("활성 쿠폰 1", "활성 쿠폰 2")
        
        val discountCoupon = availableCoupons.find { it.title == "활성 쿠폰 1" }
        assertThat(discountCoupon).isNotNull
        assertThat(discountCoupon!!.couponType).isEqualTo(CouponType.Fixed)
        assertThat(discountCoupon.value).isEqualTo(5000)
        
        val percentCoupon = availableCoupons.find { it.title == "활성 쿠폰 2" }
        assertThat(percentCoupon).isNotNull
        assertThat(percentCoupon!!.couponType).isEqualTo(CouponType.Percent)
        assertThat(percentCoupon.value).isEqualTo(15)
    }
    
    @Test
    fun `사용된 쿠폰은 사용 가능한 쿠폰 목록에 포함되지 않는다`() {
        // given
        val userId = 1L
        
        val coupon = Coupon(
            code = "USED_COUPON",
            title = "사용된 쿠폰",
            couponType = CouponType.Fixed,
            value = 3000,
            remainingCount = 10,
            endAt = LocalDateTime.now().plusDays(30),
        )
        
        val userCoupon = UserCoupon.create(userId, coupon)
        userCoupon.isUsed = true
        
        // when
        val availableCoupons = userCouponService.getAvailableUserCoupons(userId)
        
        // then
        assertThat(availableCoupons).isEmpty()
    }
    
    @Test
    fun `동일한 사용자가 동시에 쿠폰 등록 요청을 100번 하면 1회만 성공하고 99회는 실패한다`() {
        // given
        val userId = 1L
        val coupon = couponRepository.save(
            Coupon(
                code = "CONCURRENT_TEST_COUPON",
                title = "동시성 테스트 쿠폰",
                couponType = CouponType.Fixed,
                value = 1000,
                remainingCount = 100, // 충분한 수량
                endAt = LocalDateTime.now().plusDays(30),
            )
        )
        
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)
        val successCount = AtomicInteger(0)
        val failureCount = AtomicInteger(0)
        
        // when
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
        
        // then
        assertThat(successCount.get()).isEqualTo(1)
        assertThat(failureCount.get()).isEqualTo(99)
        
        val isRegistered = userCouponRepository.isRegistered(userId, coupon.id)
        assertThat(isRegistered).isTrue()
        
        val updatedCoupon = couponRepository.findById(coupon.id)
        assertThat(updatedCoupon.remainingCount).isEqualTo(99)
    }
    
    @Test
    fun `서로 다른 사용자가 동시에 동일한 쿠폰을 100번 요청하면 쿠폰 수량만큼만 발급되고 그 이후로는 발급이 안된다`() {
        // given
        val couponLimit = 10
        val requestCount = 100
        
        val coupon = couponRepository.save(
            Coupon(
                code = "LIMITED_CONCURRENT_COUPON",
                title = "제한된 동시성 테스트 쿠폰",
                couponType = CouponType.Percent,
                value = 20,
                remainingCount = couponLimit,
                endAt = LocalDateTime.now().plusDays(30),
            )
        )
        
        val executorService = Executors.newFixedThreadPool(requestCount)
        val latch = CountDownLatch(requestCount)
        val successCount = AtomicInteger(0)
        val limitedCouponFailureCount = AtomicInteger(0)
        val otherFailureCount = AtomicInteger(0)
        
        // when
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
        
        // then
        assertThat(successCount.get()).isEqualTo(couponLimit)
        assertThat(limitedCouponFailureCount.get()).isEqualTo(requestCount - couponLimit)
        assertThat(otherFailureCount.get()).isEqualTo(0)
        
        val updatedCoupon = couponRepository.findById(coupon.id)
        assertThat(updatedCoupon.remainingCount).isEqualTo(0)
        
        var registeredCount = 0
        
        for (userId in 1L..100L) {
            if (userCouponRepository.isRegistered(userId, coupon.id)) {
                registeredCount += 1
            }
        }
        
        assertThat(registeredCount).isEqualTo(couponLimit)
    }
}