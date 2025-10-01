package com.eager.questioncloud.payment.scenario

import com.eager.questioncloud.payment.domain.Coupon
import com.eager.questioncloud.payment.domain.UserCoupon
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.enums.DiscountCalculationType
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.KotlinTypeDefaultArbitraryBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import java.time.LocalDateTime

object CouponScenario {
    fun productFixedCoupon(
        questionId: Long,
        discount: Int = 1000,
        minPurchase: Int = 0
    ): Coupon {
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
            .set(Coupon::couponType, CouponType.PRODUCT_TARGET)
            .set(Coupon::discountCalculationType, DiscountCalculationType.FIXED)
            .set(Coupon::value, discount)
            .set(Coupon::maximumDiscountAmount, discount)
            .set(Coupon::minimumPurchaseAmount, minPurchase)
            .set(Coupon::isDuplicable, false)
            .set(Coupon::targetQuestionId, questionId)
            .set(Coupon::endAt, LocalDateTime.now().plusDays(30))
            .set(Coupon::remainingCount, 100)
            .sample()
    }
    
    fun productPercentCoupon(
        questionId: Long,
        percent: Int = 10,
        maxDiscount: Int = 10000,
        minPurchase: Int = 0
    ): Coupon {
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
            .set(Coupon::couponType, CouponType.PRODUCT_TARGET)
            .set(Coupon::discountCalculationType, DiscountCalculationType.PERCENT)
            .set(Coupon::value, percent)
            .set(Coupon::maximumDiscountAmount, maxDiscount)
            .set(Coupon::minimumPurchaseAmount, minPurchase)
            .set(Coupon::isDuplicable, false)
            .set(Coupon::targetQuestionId, questionId)
            .set(Coupon::endAt, LocalDateTime.now().plusDays(30))
            .set(Coupon::remainingCount, 100)
            .sample()
    }
    
    fun duplicableFixedProductCoupon(
        questionId: Long,
        discount: Int = 500,
        minPurchase: Int = 0
    ): Coupon {
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
            .set(Coupon::couponType, CouponType.PRODUCT_TARGET)
            .set(Coupon::discountCalculationType, DiscountCalculationType.FIXED)
            .set(Coupon::value, discount)
            .set(Coupon::maximumDiscountAmount, discount)
            .set(Coupon::minimumPurchaseAmount, minPurchase)
            .set(Coupon::isDuplicable, true)
            .set(Coupon::targetQuestionId, questionId)
            .set(Coupon::endAt, LocalDateTime.now().plusDays(30))
            .set(Coupon::remainingCount, 100)
            .sample()
    }
    
    fun duplicablePercentProductCoupon(
        questionId: Long,
        percent: Int = 10,
        maxDiscount: Int = 10000,
        minPurchase: Int = 0
    ): Coupon {
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
            .set(Coupon::couponType, CouponType.PRODUCT_TARGET)
            .set(Coupon::discountCalculationType, DiscountCalculationType.PERCENT)
            .set(Coupon::value, percent)
            .set(Coupon::maximumDiscountAmount, maxDiscount)
            .set(Coupon::minimumPurchaseAmount, minPurchase)
            .set(Coupon::isDuplicable, true)
            .set(Coupon::targetQuestionId, questionId)
            .set(Coupon::endAt, LocalDateTime.now().plusDays(30))
            .set(Coupon::remainingCount, 100)
            .sample()
    }
    
    fun paymentFixedCoupon(
        discount: Int = 2000,
        minPurchase: Int = 0
    ): Coupon {
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
            .set(Coupon::couponType, CouponType.PAYMENT)
            .set(Coupon::discountCalculationType, DiscountCalculationType.FIXED)
            .set(Coupon::value, discount)
            .set(Coupon::maximumDiscountAmount, discount)
            .set(Coupon::minimumPurchaseAmount, minPurchase)
            .set(Coupon::isDuplicable, false)
            .set(Coupon::targetQuestionId, null)
            .set(Coupon::endAt, LocalDateTime.now().plusDays(30))
            .set(Coupon::remainingCount, 100)
            .sample()
    }
    
    fun paymentPercentCoupon(
        percent: Int = 5,
        maxDiscount: Int = 5000,
        minPurchase: Int = 0
    ): Coupon {
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
            .set(Coupon::couponType, CouponType.PAYMENT)
            .set(Coupon::discountCalculationType, DiscountCalculationType.PERCENT)
            .set(Coupon::value, percent)
            .set(Coupon::maximumDiscountAmount, maxDiscount)
            .set(Coupon::minimumPurchaseAmount, minPurchase)
            .set(Coupon::isDuplicable, false)
            .set(Coupon::targetQuestionId, null)
            .set(Coupon::endAt, LocalDateTime.now().plusDays(30))
            .set(Coupon::remainingCount, 100)
            .sample()
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

fun Coupon.custom(block: KotlinTypeDefaultArbitraryBuilder<Coupon>.() -> KotlinTypeDefaultArbitraryBuilder<Coupon>): Coupon {
    return Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>(this)
        .block()
        .sample()
}