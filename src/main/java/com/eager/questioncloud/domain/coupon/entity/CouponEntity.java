package com.eager.questioncloud.domain.coupon.entity;

import com.eager.questioncloud.domain.coupon.model.Coupon;
import com.eager.questioncloud.domain.coupon.vo.CouponType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String code;

    @Column
    private String title;

    @Column
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column
    private int value;

    @Column
    private int remainingCount;

    @Column
    private LocalDateTime endAt;

    @Builder
    public CouponEntity(Long id, String code, String title, CouponType couponType, int value, int remainingCount, LocalDateTime endAt) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.couponType = couponType;
        this.value = value;
        this.remainingCount = remainingCount;
        this.endAt = endAt;
    }

    public Coupon toDomain() {
        return Coupon.builder()
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
