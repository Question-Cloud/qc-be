package com.eager.questioncloud.application.api.user.coupon.service

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.CouponFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserCouponFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.coupon.enums.CouponType
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
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
    @Autowired val userRepository: UserRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `사용 가능한 쿠폰 목록을 조회할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "test@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )

        val activeCoupon1 = CouponFixtureHelper.createCoupon(
            code = "ACTIVE_COUPON_1",
            title = "활성 쿠폰 1",
            couponType = CouponType.Fixed,
            value = 5000,
            remainingCount = 10,
            endAt = LocalDateTime.now().plusDays(30),
            couponRepository
        )

        val activeCoupon2 = CouponFixtureHelper.createCoupon(
            code = "ACTIVE_COUPON_2",
            title = "활성 쿠폰 2",
            couponType = CouponType.Percent,
            value = 15,
            remainingCount = 5,
            endAt = LocalDateTime.now().plusDays(15),
            couponRepository
        )

        val expiredCoupon = CouponFixtureHelper.createCoupon(
            code = "EXPIRED_COUPON",
            title = "만료된 쿠폰",
            couponType = CouponType.Fixed,
            value = 10000,
            remainingCount = 3,
            endAt = LocalDateTime.now().minusDays(1),
            couponRepository
        )

        UserCouponFixtureHelper.createUserCoupon(activeCoupon1, user.uid, userCouponRepository)
        UserCouponFixtureHelper.createUserCoupon(activeCoupon2, user.uid, userCouponRepository)
        UserCouponFixtureHelper.createUserCoupon(expiredCoupon, user.uid, userCouponRepository)

        // when
        val availableCoupons = userCouponService.getAvailableUserCoupons(user.uid)

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
    fun `사용 가능한 쿠폰이 없는 사용자는 빈 목록을 반환한다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "empty@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )

        // when
        val availableCoupons = userCouponService.getAvailableUserCoupons(user.uid)

        // then
        assertThat(availableCoupons).isEmpty()
    }

    @Test
    fun `사용된 쿠폰은 사용 가능한 쿠폰 목록에 포함되지 않는다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "used@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )

        val coupon = CouponFixtureHelper.createCoupon(
            code = "USED_COUPON",
            title = "사용된 쿠폰",
            couponType = CouponType.Fixed,
            value = 3000,
            remainingCount = 10,
            endAt = LocalDateTime.now().plusDays(30),
            couponRepository
        )

        val userCoupon = UserCouponFixtureHelper.createUserCoupon(coupon, user.uid, userCouponRepository)
        userCoupon.isUsed = true
        userCouponRepository.save(userCoupon)

        // when
        val availableCoupons = userCouponService.getAvailableUserCoupons(user.uid)

        // then
        assertThat(availableCoupons).isEmpty()
    }

    @Test
    fun `여러 사용자가 각각 다른 쿠폰을 가지고 있을 때 올바른 쿠폰만 조회된다`() {
        // given
        val user1 = UserFixtureHelper.createEmailUser(
            "user1@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val user2 = UserFixtureHelper.createEmailUser(
            "user2@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )

        val coupon1 = CouponFixtureHelper.createCoupon(
            code = "USER1_COUPON",
            title = "사용자1 쿠폰",
            couponType = CouponType.Fixed,
            value = 5000,
            remainingCount = 10,
            endAt = LocalDateTime.now().plusDays(30),
            couponRepository
        )

        val coupon2 = CouponFixtureHelper.createCoupon(
            code = "USER2_COUPON",
            title = "사용자2 쿠폰",
            couponType = CouponType.Percent,
            value = 20,
            remainingCount = 5,
            endAt = LocalDateTime.now().plusDays(15),
            couponRepository
        )

        UserCouponFixtureHelper.createUserCoupon(coupon1, user1.uid, userCouponRepository)
        UserCouponFixtureHelper.createUserCoupon(coupon2, user2.uid, userCouponRepository)

        // when
        val user1Coupons = userCouponService.getAvailableUserCoupons(user1.uid)
        val user2Coupons = userCouponService.getAvailableUserCoupons(user2.uid)

        // then
        assertThat(user1Coupons).hasSize(1)
        assertThat(user1Coupons[0].title).isEqualTo("사용자1 쿠폰")

        assertThat(user2Coupons).hasSize(1)
        assertThat(user2Coupons[0].title).isEqualTo("사용자2 쿠폰")
    }

    @Test
    fun `동일한 사용자가 동시에 쿠폰 등록 요청을 100번 하면 1회만 성공하고 99회는 실패한다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "concurrent_user@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val coupon = CouponFixtureHelper.createCoupon(
            code = "CONCURRENT_TEST_COUPON",
            title = "동시성 테스트 쿠폰",
            couponType = CouponType.Fixed,
            value = 1000,
            remainingCount = 100, // 충분한 수량
            endAt = LocalDateTime.now().plusDays(30),
            couponRepository
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
                    userCouponService.registerCoupon(user.uid, coupon.code)
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

        val isRegistered = userCouponRepository.isRegistered(user.uid, coupon.id)
        assertThat(isRegistered).isTrue()

        val updatedCoupon = couponRepository.findById(coupon.id)
        assertThat(updatedCoupon.remainingCount).isEqualTo(99)
    }

    @Test
    fun `서로 다른 사용자가 동시에 동일한 쿠폰을 100번 요청하면 쿠폰 수량만큼만 발급되고 그 이후로는 발급이 안된다`() {
        // given
        val couponLimit = 10
        val requestCount = 100

        val coupon = CouponFixtureHelper.createCoupon(
            code = "LIMITED_CONCURRENT_COUPON",
            title = "제한된 동시성 테스트 쿠폰",
            couponType = CouponType.Percent,
            value = 20,
            remainingCount = couponLimit,
            endAt = LocalDateTime.now().plusDays(30),
            couponRepository
        )

        val users = (1..requestCount).map { index ->
            UserFixtureHelper.createEmailUser(
                "user$index@example.com",
                "password123",
                UserStatus.Active,
                userRepository
            )
        }

        val executorService = Executors.newFixedThreadPool(requestCount)
        val latch = CountDownLatch(requestCount)
        val successCount = AtomicInteger(0)
        val limitedCouponFailureCount = AtomicInteger(0)
        val otherFailureCount = AtomicInteger(0)

        // when
        users.forEach { user ->
            executorService.submit {
                runCatching {
                    userCouponService.registerCoupon(user.uid, coupon.code)
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

        val registeredCount = users.count { user ->
            userCouponRepository.isRegistered(user.uid, coupon.id)
        }

        assertThat(registeredCount).isEqualTo(couponLimit)
    }
}
