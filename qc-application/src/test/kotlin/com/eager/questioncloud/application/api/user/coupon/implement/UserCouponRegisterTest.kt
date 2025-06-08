package com.eager.questioncloud.application.api.user.coupon.implement

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.CouponFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserCouponFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.coupon.enums.CouponType
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
class UserCouponRegisterTest(
    @Autowired val userCouponRegister: UserCouponRegister,
    @Autowired val couponRepository: CouponRepository,
    @Autowired val userCouponRepository: UserCouponRepository,
    @Autowired val userRepository: UserRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `쿠폰을 등록할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "test@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val coupon = CouponFixtureHelper.createCoupon(
            code = "TEST_COUPON_CODE",
            title = "테스트 쿠폰",
            couponType = CouponType.Fixed,
            value = 5000,
            remainingCount = 10,
            endAt = LocalDateTime.now().plusDays(30),
            couponRepository
        )

        // when
        userCouponRegister.registerCoupon(user.uid, coupon.code)

        // then
        val isRegistered = userCouponRepository.isRegistered(user.uid, coupon.id)
        Assertions.assertThat(isRegistered).isTrue()

        val updatedCoupon = couponRepository.findById(coupon.id)
        Assertions.assertThat(updatedCoupon.remainingCount).isEqualTo(9)
    }

    @Test
    fun `이미 등록한 쿠폰을 다시 등록하려고 하면 예외가 발생한다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "test@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val coupon = CouponFixtureHelper.createCoupon(
            code = "DUPLICATE_COUPON",
            title = "중복 테스트 쿠폰",
            couponType = CouponType.Fixed,
            value = 3000,
            remainingCount = 5,
            endAt = LocalDateTime.now().plusDays(15),
            couponRepository
        )
        
        UserCouponFixtureHelper.createUserCoupon(coupon, user.uid, userCouponRepository)

        // when & then
        Assertions.assertThatThrownBy {
            userCouponRegister.registerCoupon(user.uid, coupon.code)
        }.isInstanceOf(CoreException::class.java)
            .hasMessage(Error.ALREADY_REGISTER_COUPON.message)
    }

    @Test
    fun `수량이 부족한 쿠폰을 등록하려고 하면 예외가 발생한다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "test@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val coupon = CouponFixtureHelper.createCoupon(
            code = "LIMITED_COUPON",
            title = "수량 부족 쿠폰",
            couponType = CouponType.Fixed,
            value = 10000,
            remainingCount = 0,
            endAt = LocalDateTime.now().plusDays(30),
            couponRepository
        )

        // when & then
        Assertions.assertThatThrownBy {
            userCouponRegister.registerCoupon(user.uid, coupon.code)
        }.isInstanceOf(CoreException::class.java)
            .hasMessage(Error.LIMITED_COUPON.message)
    }

    @Test
    fun `존재하지 않는 쿠폰을 등록하려고 하면 예외가 발생한다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "test@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )

        // when & then
        Assertions.assertThatThrownBy {
            userCouponRegister.registerCoupon(user.uid, "INVALID_COUPON_CODE")
        }.isInstanceOf(CoreException::class.java)
    }

    @Test
    fun `여러 사용자가 동일한 쿠폰을 등록할 수 있다`() {
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
        val coupon = CouponFixtureHelper.createCoupon(
            code = "SHARED_COUPON",
            title = "공유 쿠폰",
            couponType = CouponType.Percent,
            value = 20,
            remainingCount = 10,
            endAt = LocalDateTime.now().plusDays(30),
            couponRepository
        )

        // when
        userCouponRegister.registerCoupon(user1.uid, coupon.code)
        userCouponRegister.registerCoupon(user2.uid, coupon.code)

        // then
        val user1IsRegistered = userCouponRepository.isRegistered(user1.uid, coupon.id)
        val user2IsRegistered = userCouponRepository.isRegistered(user2.uid, coupon.id)

        Assertions.assertThat(user1IsRegistered).isTrue()
        Assertions.assertThat(user2IsRegistered).isTrue()

        val updatedCoupon = couponRepository.findById(coupon.id)
        Assertions.assertThat(updatedCoupon.remainingCount).isEqualTo(8)
    }
}
