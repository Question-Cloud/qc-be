package com.eager.questioncloud.core.domain.coupon.model;

import com.eager.questioncloud.core.domain.coupon.enums.CouponType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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
}
