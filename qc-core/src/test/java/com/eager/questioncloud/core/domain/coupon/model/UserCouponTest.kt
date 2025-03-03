package com.eager.questioncloud.core.domain.coupon.model

import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class UserCouponTest {
    @Test
    @DisplayName("사용기한이 지난 쿠폰은 사용할 수 없다.")
    fun cannotUseExpiredCoupon() {
        // given
        val expiredUserCoupon = Fixture.fixtureMonkey.giveMeKotlinBuilder<UserCoupon>()
            .set(UserCoupon::isUsed, false)
            .set(UserCoupon::endAt, LocalDateTime.now().minusDays(10))
            .sample()

        // when then
        Assertions.assertThatThrownBy { expiredUserCoupon.validate() }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.EXPIRED_COUPON)
    }

    @Test
    @DisplayName("이미 사용한 쿠폰은 사용할 수 없다.")
    fun cannotUseAlreadyUsedCoupon() {
        // given
        val usedCoupon = Fixture.fixtureMonkey.giveMeKotlinBuilder<UserCoupon>()
            .set(UserCoupon::isUsed, true)
            .set(UserCoupon::endAt, LocalDateTime.now().plusDays(10))
            .sample()

        // when then
        Assertions.assertThatThrownBy { usedCoupon.validate() }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_USE_COUPON)
    }
}