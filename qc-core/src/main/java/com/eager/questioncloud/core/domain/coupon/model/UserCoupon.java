package com.eager.questioncloud.core.domain.coupon.model;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCoupon {
    private Long id;
    private Long userId;
    private Long couponId;
    private Boolean isUsed;
    private LocalDateTime createdAt;
    private LocalDateTime endAt;

    @Builder
    public UserCoupon(Long id, Long userId, Long couponId, Boolean isUsed, LocalDateTime createdAt, LocalDateTime endAt) {
        this.id = id;
        this.userId = userId;
        this.couponId = couponId;
        this.isUsed = isUsed;
        this.createdAt = createdAt;
        this.endAt = endAt;
    }

    public void use() {
        if (endAt.isBefore(LocalDateTime.now())) {
            throw new CustomException(Error.EXPIRED_COUPON);
        }
        this.isUsed = true;
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
            .couponId(coupon.getId())
            .isUsed(false)
            .createdAt(LocalDateTime.now())
            .endAt(coupon.getEndAt())
            .build();
    }
}
