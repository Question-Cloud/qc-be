package com.eager.questioncloud.application.utils.fixture.helper

import com.eager.questioncloud.application.utils.fixture.Fixture
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.core.domain.coupon.model.Coupon
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

class UserCouponFixtureHelper {
    companion object {
        fun createUserCoupon(coupon: Coupon, uid: Long, userCouponRepository: UserCouponRepository): UserCoupon {
            return userCouponRepository.save(
                Fixture.fixtureMonkey.giveMeKotlinBuilder<UserCoupon>()
                    .set(UserCoupon::couponId, coupon.id)
                    .set(UserCoupon::userId, uid)
                    .set(UserCoupon::endAt, coupon.endAt)
                    .set(UserCoupon::isUsed, false)
                    .sample()
            )
        }
    }
}