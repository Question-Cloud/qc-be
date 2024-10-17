package com.eager.questioncloud.point.model;

import com.eager.questioncloud.point.entity.ChargePointOrderEntity;
import com.eager.questioncloud.point.vo.ChargePointOrderStatus;
import com.eager.questioncloud.point.vo.ChargePointType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChargePointOrder {
    private Long id;
    private Long userId;
    private String paymentId;
    private ChargePointType chargePointType;
    private ChargePointOrderStatus chargePointOrderStatus;
    private LocalDateTime createdAt;

    @Builder
    public ChargePointOrder(Long id, Long userId, String paymentId, ChargePointType chargePointType, ChargePointOrderStatus chargePointOrderStatus,
        LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.paymentId = paymentId;
        this.chargePointType = chargePointType;
        this.chargePointOrderStatus = chargePointOrderStatus;
        this.createdAt = createdAt;
    }

    public ChargePointOrderEntity toEntity() {
        return ChargePointOrderEntity.builder()
            .id(id)
            .userId(userId)
            .paymentId(paymentId)
            .chargePointType(chargePointType)
            .chargePointOrderStatus(chargePointOrderStatus)
            .createdAt(createdAt)
            .build();
    }
}
