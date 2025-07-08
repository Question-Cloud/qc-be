package com.eager.questioncloud.pay.coupon.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.Coupon
import com.eager.questioncloud.payment.domain.UserCoupon
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.infrastructure.repository.CouponRepository
import com.eager.questioncloud.payment.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.utils.DBCleaner
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
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `쿠폰을 등록할 수 있다`() {
        // given
        val userId = 1L
        
        val coupon = couponRepository.save(
            Coupon(
                code = "coupon-code",
                title = "할인 쿠폰 1000",
                couponType = CouponType.Fixed,
                value = 1000,
                remainingCount = 100,
                endAt = LocalDateTime.now().plusDays(10)
            )
        )
        
        // when
        userCouponRegister.registerCoupon(userId, coupon.code)
        
        // then
        val isRegistered = userCouponRepository.isRegistered(userId, coupon.id)
        Assertions.assertThat(isRegistered).isTrue()
        
        val updatedCoupon = couponRepository.findById(coupon.id)
        Assertions.assertThat(updatedCoupon.remainingCount).isEqualTo(coupon.remainingCount - 1)
    }
    
    @Test
    fun `이미 등록한 쿠폰을 다시 등록하려고 하면 예외가 발생한다`() {
        // given
        val userId = 1L
        val coupon = couponRepository.save(
            Coupon(
                code = "coupon-code",
                title = "할인 쿠폰 1000",
                couponType = CouponType.Fixed,
                value = 1000,
                remainingCount = 100,
                endAt = LocalDateTime.now().plusDays(10)
            )
        )
        userCouponRepository.save(UserCoupon.create(userId, coupon))
        
        // when & then
        Assertions.assertThatThrownBy {
            userCouponRegister.registerCoupon(userId, coupon.code)
        }.isInstanceOf(CoreException::class.java)
            .hasMessage(Error.ALREADY_REGISTER_COUPON.message)
    }
    
    @Test
    fun `수량이 부족한 쿠폰을 등록하려고 하면 예외가 발생한다`() {
        // given
        val userId = 1L
        val coupon = couponRepository.save(
            Coupon(
                code = "coupon-code",
                title = "할인 쿠폰 1000",
                couponType = CouponType.Fixed,
                value = 1000,
                remainingCount = 0,
                endAt = LocalDateTime.now().plusDays(10)
            )
        )
        
        // when & then
        Assertions.assertThatThrownBy {
            userCouponRegister.registerCoupon(userId, coupon.code)
        }.isInstanceOf(CoreException::class.java)
            .hasMessage(Error.LIMITED_COUPON.message)
    }
    
    @Test
    fun `존재하지 않는 쿠폰을 등록하려고 하면 예외가 발생한다`() {
        // given
        val userId = 1L
        
        // when & then
        Assertions.assertThatThrownBy {
            userCouponRegister.registerCoupon(userId, "INVALID_COUPON_CODE")
        }.isInstanceOf(CoreException::class.java)
    }
    
    @Test
    fun `여러 사용자가 동일한 쿠폰을 등록할 수 있다`() {
        // given
        val user1 = 1L
        val user2 = 2L
        val coupon = couponRepository.save(
            Coupon(
                code = "coupon-code",
                title = "할인 쿠폰 1000",
                couponType = CouponType.Fixed,
                value = 1000,
                remainingCount = 100,
                endAt = LocalDateTime.now().plusDays(10)
            )
        )
        
        // when
        userCouponRegister.registerCoupon(user1, coupon.code)
        userCouponRegister.registerCoupon(user2, coupon.code)
        
        // then
        val user1IsRegistered = userCouponRepository.isRegistered(user1, coupon.id)
        val user2IsRegistered = userCouponRepository.isRegistered(user2, coupon.id)
        
        Assertions.assertThat(user1IsRegistered).isTrue()
        Assertions.assertThat(user2IsRegistered).isTrue()
        
        val updatedCoupon = couponRepository.findById(coupon.id)
        Assertions.assertThat(updatedCoupon.remainingCount).isEqualTo(coupon.remainingCount - 2)
    }
}