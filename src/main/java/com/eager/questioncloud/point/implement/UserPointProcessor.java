package com.eager.questioncloud.point.implement;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.exception.InvalidPaymentException;
import com.eager.questioncloud.point.domain.ChargePointType;
import com.eager.questioncloud.point.domain.UserPointPayment;
import com.eager.questioncloud.portone.dto.PortonePayment;
import com.eager.questioncloud.portone.enums.PortonePaymentStatus;
import com.eager.questioncloud.portone.implement.PortoneAPI;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointProcessor {
    private final UserPointPaymentReader userPointPaymentReader;
    private final UserPointPaymentAppender userPointPaymentAppender;
    private final PortoneAPI portoneAPI;
    private final UserPointReader userPointReader;
    private final UserPointUpdater userPointUpdater;

    @Transactional
    public void chargePoint(Long userId, ChargePointType chargePointType, String paymentId) {
        PortonePayment portonePayment = portoneAPI.getPaymentResult(paymentId);
        validatePayment(portonePayment, chargePointType);

        int userPoint = userPointReader.getUserPoint(userId);
        userPointUpdater.updateUserPoint(userId, userPoint + chargePointType.getAmount());

        userPointPaymentAppender.append(UserPointPayment.create(userId, chargePointType, portonePayment));
    }

    private void validatePayment(PortonePayment portonePayment, ChargePointType chargePointType) {
        if (userPointPaymentReader.existsById(portonePayment.getId())) {
            throw new CustomException(Error.ALREADY_PROCESSED_PAYMENT);
        }

        if (!portonePayment.getStatus().equals(PortonePaymentStatus.PAID)) {
            throw new CustomException(Error.NOT_PROCESS_PAYMENT);
        }

        if (portonePayment.getAmount().getTotal() != chargePointType.getAmount()) {
            throw new InvalidPaymentException(portonePayment);
        }
    }

    public void usePoint(Long userId, int amount) {
        int userPoint = userPointReader.getUserPoint(userId);
        if (userPoint < amount) {
            throw new CustomException(Error.NOT_ENOUGH_POINT);
        }
        userPointUpdater.updateUserPoint(userId, userPoint - amount);
    }
}
