package com.eager.questioncloud.core.domain.coupon.model;

import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.utils.Fixture;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserCouponTest {
    @Test
    @DisplayName("사용기한이 지난 쿠폰은 사용할 수 없다.")
    void cannotUseExpiredCoupon() {
        // given
        UserCoupon expireduserCoupon = Fixture.fixtureMonkey.giveMeBuilder(UserCoupon.class)
            .set("isUsed", false)
            .set("endAt", LocalDateTime.now().minusDays(10))
            .sample();

        // when then
        Assertions.assertThatThrownBy(expireduserCoupon::validate)
            .isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("error", Error.EXPIRED_COUPON);
    }

    @Test
    @DisplayName("이미 사용한 쿠폰은 사용할 수 없다.")
    void cannotUseAlreadyUsedCoupon() {
        // given
        UserCoupon usedCoupon = Fixture.fixtureMonkey.giveMeBuilder(UserCoupon.class)
            .set("isUsed", true)
            .set("endAt", LocalDateTime.now().plusDays(10))
            .sample();

        // when then
        Assertions.assertThatThrownBy(usedCoupon::validate)
            .isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_USE_COUPON);
    }
}