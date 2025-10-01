package com.eager.questioncloud.payment.scenario

import com.eager.questioncloud.payment.domain.CouponInformation
import com.eager.questioncloud.payment.domain.UserCoupon
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.enums.DiscountCalculationType
import com.eager.questioncloud.payment.repository.CouponInformationRepository
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
    ): CouponInformation {
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<CouponInformation>()
            .set(CouponInformation::couponType, CouponType.PRODUCT_TARGET)
            .set(CouponInformation::discountCalculationType, DiscountCalculationType.FIXED)
            .set(CouponInformation::value, discount)
            .set(CouponInformation::maximumDiscountAmount, discount)
            .set(CouponInformation::minimumPurchaseAmount, minPurchase)
            .set(CouponInformation::isDuplicable, false)
            .set(CouponInformation::targetQuestionId, questionId)
            .set(CouponInformation::endAt, LocalDateTime.now().plusDays(30))
            .set(CouponInformation::remainingCount, 100)
            .sample()
    }
    
    fun productPercentCoupon(
        questionId: Long,
        percent: Int = 10,
        maxDiscount: Int = 10000,
        minPurchase: Int = 0
    ): CouponInformation {
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<CouponInformation>()
            .set(CouponInformation::couponType, CouponType.PRODUCT_TARGET)
            .set(CouponInformation::discountCalculationType, DiscountCalculationType.PERCENT)
            .set(CouponInformation::value, percent)
            .set(CouponInformation::maximumDiscountAmount, maxDiscount)
            .set(CouponInformation::minimumPurchaseAmount, minPurchase)
            .set(CouponInformation::isDuplicable, false)
            .set(CouponInformation::targetQuestionId, questionId)
            .set(CouponInformation::endAt, LocalDateTime.now().plusDays(30))
            .set(CouponInformation::remainingCount, 100)
            .sample()
    }
    
    fun duplicableFixedProductCoupon(
        questionId: Long,
        discount: Int = 500,
        minPurchase: Int = 0
    ): CouponInformation {
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<CouponInformation>()
            .set(CouponInformation::couponType, CouponType.PRODUCT_TARGET)
            .set(CouponInformation::discountCalculationType, DiscountCalculationType.FIXED)
            .set(CouponInformation::value, discount)
            .set(CouponInformation::maximumDiscountAmount, discount)
            .set(CouponInformation::minimumPurchaseAmount, minPurchase)
            .set(CouponInformation::isDuplicable, true)
            .set(CouponInformation::targetQuestionId, questionId)
            .set(CouponInformation::endAt, LocalDateTime.now().plusDays(30))
            .set(CouponInformation::remainingCount, 100)
            .sample()
    }
    
    fun duplicablePercentProductCoupon(
        questionId: Long,
        percent: Int = 10,
        maxDiscount: Int = 10000,
        minPurchase: Int = 0
    ): CouponInformation {
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<CouponInformation>()
            .set(CouponInformation::couponType, CouponType.PRODUCT_TARGET)
            .set(CouponInformation::discountCalculationType, DiscountCalculationType.PERCENT)
            .set(CouponInformation::value, percent)
            .set(CouponInformation::maximumDiscountAmount, maxDiscount)
            .set(CouponInformation::minimumPurchaseAmount, minPurchase)
            .set(CouponInformation::isDuplicable, true)
            .set(CouponInformation::targetQuestionId, questionId)
            .set(CouponInformation::endAt, LocalDateTime.now().plusDays(30))
            .set(CouponInformation::remainingCount, 100)
            .sample()
    }
    
    fun paymentFixedCoupon(
        discount: Int = 2000,
        minPurchase: Int = 0
    ): CouponInformation {
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<CouponInformation>()
            .set(CouponInformation::couponType, CouponType.PAYMENT)
            .set(CouponInformation::discountCalculationType, DiscountCalculationType.FIXED)
            .set(CouponInformation::value, discount)
            .set(CouponInformation::maximumDiscountAmount, discount)
            .set(CouponInformation::minimumPurchaseAmount, minPurchase)
            .set(CouponInformation::isDuplicable, false)
            .set(CouponInformation::targetQuestionId, null)
            .set(CouponInformation::endAt, LocalDateTime.now().plusDays(30))
            .set(CouponInformation::remainingCount, 100)
            .sample()
    }
    
    fun paymentPercentCoupon(
        percent: Int = 5,
        maxDiscount: Int = 5000,
        minPurchase: Int = 0
    ): CouponInformation {
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<CouponInformation>()
            .set(CouponInformation::couponType, CouponType.PAYMENT)
            .set(CouponInformation::discountCalculationType, DiscountCalculationType.PERCENT)
            .set(CouponInformation::value, percent)
            .set(CouponInformation::maximumDiscountAmount, maxDiscount)
            .set(CouponInformation::minimumPurchaseAmount, minPurchase)
            .set(CouponInformation::isDuplicable, false)
            .set(CouponInformation::targetQuestionId, null)
            .set(CouponInformation::endAt, LocalDateTime.now().plusDays(30))
            .set(CouponInformation::remainingCount, 100)
            .sample()
    }
}

fun CouponInformation.save(couponInformationRepository: CouponInformationRepository): CouponInformation {
    return couponInformationRepository.save(this)
}

fun CouponInformation.setUserCoupon(userId: Long, userCouponRepository: UserCouponRepository, isUsed: Boolean = false): UserCoupon {
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

fun CouponInformation.custom(block: KotlinTypeDefaultArbitraryBuilder<CouponInformation>.() -> KotlinTypeDefaultArbitraryBuilder<CouponInformation>): CouponInformation {
    return Fixture.fixtureMonkey.giveMeKotlinBuilder<CouponInformation>(this)
        .block()
        .sample()
}