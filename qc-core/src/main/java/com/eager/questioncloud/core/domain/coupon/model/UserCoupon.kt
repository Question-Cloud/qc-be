package com.eager.questioncloud.core.domain.coupon.model;

import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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

    public void validate() {
        if (endAt.isBefore(LocalDateTime.now())) {
            throw new CoreException(Error.EXPIRED_COUPON);
        }

        if (this.isUsed) {
            throw new CoreException(Error.FAIL_USE_COUPON);
        }
    }

    public void rollback() {
        this.isUsed = false;
    }

    public static UserCoupon create(Long userId, Coupon coupon) {
        if (coupon.getEndAt().isBefore(LocalDateTime.now())) {
            throw new CoreException(Error.EXPIRED_COUPON);
        }
        if (coupon.getRemainingCount() == 0) {
            throw new CoreException(Error.LIMITED_COUPON);
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
