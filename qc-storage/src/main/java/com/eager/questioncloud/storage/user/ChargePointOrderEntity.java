package com.eager.questioncloud.storage.user;

import com.eager.questioncloud.core.domain.user.model.ChargePointOrder;
import com.eager.questioncloud.core.domain.user.vo.ChargePointOrderStatus;
import com.eager.questioncloud.core.domain.user.vo.ChargePointType;
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
@Table(name = "charge_point_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChargePointOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private String paymentId;

    @Column
    @Enumerated(EnumType.STRING)
    private ChargePointType chargePointType;

    @Column
    @Enumerated(EnumType.STRING)
    private ChargePointOrderStatus chargePointOrderStatus;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public ChargePointOrderEntity(Long id, Long userId, String paymentId, ChargePointType chargePointType,
        ChargePointOrderStatus chargePointOrderStatus, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.paymentId = paymentId;
        this.chargePointType = chargePointType;
        this.chargePointOrderStatus = chargePointOrderStatus;
        this.createdAt = createdAt;
    }

    public ChargePointOrder toModel() {
        return ChargePointOrder.builder()
            .id(id)
            .userId(userId)
            .paymentId(paymentId)
            .chargePointType(chargePointType)
            .chargePointOrderStatus(chargePointOrderStatus)
            .createdAt(createdAt)
            .build();
    }

    public static ChargePointOrderEntity from(ChargePointOrder chargePointOrder) {
        return ChargePointOrderEntity.builder()
            .id(chargePointOrder.getId())
            .userId(chargePointOrder.getUserId())
            .paymentId(chargePointOrder.getPaymentId())
            .chargePointType(chargePointOrder.getChargePointType())
            .chargePointOrderStatus(chargePointOrder.getChargePointOrderStatus())
            .createdAt(chargePointOrder.getCreatedAt())
            .build();
    }
}
