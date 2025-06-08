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
            code: String? = null,
            title: String? = null,
            couponType: CouponType = CouponType.Fixed,
            value: Int = 1000,
            remainingCount: Int = 10,
            endAt: LocalDateTime = LocalDateTime.now().plusDays(10),
            couponRepository: CouponRepository
        ): Coupon {
            val builder = Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
                .set(Coupon::id, null)
                .setNotNull(Coupon::code)
                .setNotNull(Coupon::title)
                .set(Coupon::couponType, couponType)
                .set(Coupon::value, value)
                .set(Coupon::remainingCount, remainingCount)
                .set(Coupon::endAt, endAt)
            
            code?.let { builder.set(Coupon::code, it) }
            title?.let { builder.set(Coupon::title, it) }
            
            return couponRepository.save(builder.sample())
        }
    }
}