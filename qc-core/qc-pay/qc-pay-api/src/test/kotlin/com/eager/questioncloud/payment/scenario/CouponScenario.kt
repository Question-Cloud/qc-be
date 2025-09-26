package com.eager.questioncloud.payment.scenario

import com.eager.questioncloud.payment.domain.Coupon
import com.eager.questioncloud.payment.domain.UserCoupon
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import java.time.LocalDateTime

class CouponScenario(
    val coupon: Coupon,
) {
    companion object {
        fun available(type: CouponType = CouponType.Fixed): CouponScenario {
            val value = if (type == CouponType.Fixed) 1000 else 10
            val coupon = Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
                .set(Coupon::endAt, LocalDateTime.now().plusDays(10))
                .set(Coupon::remainingCount, 10)
                .set(Coupon::value, value)
                .set(Coupon::couponType, type)
                .sample()
            return CouponScenario(coupon)
        }
        
        fun expired(type: CouponType = CouponType.Fixed): CouponScenario {
            val value = if (type == CouponType.Fixed) 1000 else 10
            
            val coupon = Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
                .set(Coupon::endAt, LocalDateTime.now().minusDays(10))
                .set(Coupon::remainingCount, 10)
                .set(Coupon::value, value)
                .set(Coupon::couponType, type)
                .sample()
            
            return CouponScenario(coupon)
        }
        
        fun limited(type: CouponType = CouponType.Fixed): CouponScenario {
            val value = if (type == CouponType.Fixed) 1000 else 10
            
            val coupont = Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
                .set(Coupon::endAt, LocalDateTime.now().minusDays(10))
                .set(Coupon::remainingCount, 0)
                .set(Coupon::value, value)
                .set(Coupon::couponType, type)
                .sample()
            
            return CouponScenario(coupont)
        }
    }
}

fun Coupon.save(couponRepository: CouponRepository): Coupon {
    return couponRepository.save(this)
}

fun Coupon.setUserCoupon(userId: Long, userCouponRepository: UserCouponRepository, isUsed: Boolean = false): UserCoupon {
    return userCouponRepository.save(
        UserCoupon(
            userId = userId,
            couponId = id,
            isUsed = isUsed,
            createdAt = LocalDateTime.now(),
            endAt = endAt,
        )
    )
}