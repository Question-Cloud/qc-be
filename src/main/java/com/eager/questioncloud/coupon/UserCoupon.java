package com.eager.questioncloud.coupon;

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

    @Builder
    public UserCoupon(Long id, Long userId, Long couponId, Boolean isUsed, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.couponId = couponId;
        this.isUsed = isUsed;
        this.createdAt = createdAt;
    }

    public UserCouponEntity toEntity() {
        return UserCouponEntity.builder()
            .id(id)
            .userId(userId)
            .couponId(couponId)
            .isUsed(isUsed)
            .createdAt(createdAt)
            .build();
    }
}
