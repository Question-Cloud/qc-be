package com.eager.questioncloud.application.utils.fixture.helper

import com.eager.questioncloud.application.utils.fixture.Fixture
import com.eager.questioncloud.core.domain.coupon.enums.CouponType
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository
import com.eager.questioncloud.core.domain.coupon.model.Coupon
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import java.time.LocalDateTime

class CouponFixtureHelper {
    companion object {
        fun createCoupon(
            couponType: CouponType = CouponType.Fixed,
            value: Int = 1000,
            endAt: LocalDateTime = LocalDateTime.now().plusDays(10),
            couponRepository: CouponRepository
        ): Coupon {
            return couponRepository.save(
                Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
                    .set(Coupon::id, null)
                    .set(Coupon::couponType, couponType)
                    .set(Coupon::value, value)
                    .set(Coupon::endAt, endAt)
                    .sample()
            )
        }
    }
}