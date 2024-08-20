package com.eager.questioncloud.coupon;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Coupon {
    private Long id;
    private String code;
    private String title;
    private CouponType couponType;
    private int value;
    private int remainingCount;
    private LocalDateTime endAt;

    @Builder
    public Coupon(Long id, String code, String title, CouponType couponType, int value, int remainingCount, LocalDateTime endAt) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.couponType = couponType;
        this.value = value;
        this.remainingCount = remainingCount;
        this.endAt = endAt;
    }

    public void decrease() {
        this.remainingCount = this.remainingCount - 1;
    }

    public CouponEntity toEntity() {
        return CouponEntity.builder()
            .id(id)
            .code(code)
            .title(title)
            .couponType(couponType)
            .value(value)
            .remainingCount(remainingCount)
            .endAt(endAt)
            .build();
    }
}