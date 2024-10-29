package com.eager.questioncloud.core.domain.user.point.model;

import com.eager.questioncloud.core.domain.portone.dto.PortonePayment;
import com.eager.questioncloud.core.domain.portone.enums.PortonePaymentStatus;
import com.eager.questioncloud.core.domain.user.point.vo.ChargePointOrderStatus;
import com.eager.questioncloud.core.domain.user.point.vo.ChargePointType;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
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

    public static ChargePointOrder crateOrder(Long userId, String paymentId, ChargePointType chargePointType) {
        return ChargePointOrder.builder()
            .userId(userId)
            .paymentId(paymentId)
            .chargePointType(chargePointType)
            .chargePointOrderStatus(ChargePointOrderStatus.ORDERED)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public void paid(PortonePayment portonePayment) {
        if (chargePointOrderStatus.equals(ChargePointOrderStatus.PAID)) {
            throw new CustomException(Error.ALREADY_PROCESSED_PAYMENT);
        }

        if (!portonePayment.getStatus().equals(PortonePaymentStatus.PAID)) {
            throw new CustomException(Error.NOT_PROCESS_PAYMENT);
        }
//TODO Exception 처리

//        if (portonePayment.getAmount().getTotal() != chargePointType.getAmount()) {
//            throw new InvalidPaymentException(portonePayment);
//        }

        this.chargePointOrderStatus = ChargePointOrderStatus.PAID;
    }
}
