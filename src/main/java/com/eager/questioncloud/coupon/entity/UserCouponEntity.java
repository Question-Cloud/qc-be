package com.eager.questioncloud.coupon.entity;

import com.eager.questioncloud.coupon.model.UserCoupon;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private Long couponId;

    @Column
    private Boolean isUsed;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime endAt;

    @Builder
    public UserCouponEntity(Long id, Long userId, Long couponId, Boolean isUsed, LocalDateTime createdAt, LocalDateTime endAt) {
        this.id = id;
        this.userId = userId;
        this.couponId = couponId;
        this.isUsed = isUsed;
        this.createdAt = createdAt;
        this.endAt = endAt;
    }

    public UserCoupon toModel() {
        return UserCoupon.builder()
            .id(id)
            .userId(userId)
            .couponId(couponId)
            .isUsed(isUsed)
            .createdAt(createdAt)
            .endAt(endAt)
            .build();
    }
}
