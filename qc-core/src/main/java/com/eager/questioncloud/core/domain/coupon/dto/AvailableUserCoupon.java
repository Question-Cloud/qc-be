package com.eager.questioncloud.core.domain.coupon.dto;

import com.eager.questioncloud.core.domain.coupon.vo.CouponType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AvailableUserCoupon {
    private Long id;
    private String title;
    private CouponType couponType;
    private int value;
    private LocalDateTime endAt;
}
