package com.eager.questioncloud.core.domain.coupon.model;

import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCoupon {
    private Long id;
    private Long userId;
    private Coupon coupon;
    private Boolean isUsed;
    private LocalDateTime createdAt;

    @Builder
    public UserCoupon(Long id, Long userId, Coupon coupon, Boolean isUsed, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.coupon = coupon;
        this.isUsed = isUsed;
        this.createdAt = createdAt;
    }

    public void use() {
        if (coupon.getEndAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(Error.EXPIRED_COUPON);
        }
        this.isUsed = true;
    }

    public void rollback() {
        this.isUsed = false;
    }

    public static UserCoupon create(Long userId, Coupon coupon) {
        if (coupon.getEndAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(Error.EXPIRED_COUPON);
        }
        if (coupon.getRemainingCount() == 0) {
            throw new CustomException(Error.LIMITED_COUPON);
        }
        return UserCoupon.builder()
            .userId(userId)
            .coupon(coupon)
            .isUsed(false)
            .createdAt(LocalDateTime.now())
            .build();
    }
}
