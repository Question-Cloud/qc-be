package com.eager.questioncloud.point;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.exception.InvalidPaymentException;
import com.eager.questioncloud.portone.PortoneAPI;
import com.eager.questioncloud.portone.PortonePayment;
import com.eager.questioncloud.portone.PortonePaymentStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointCharger {
    private final UserPointPaymentReader userPointPaymentReader;
    private final UserPointPaymentAppender userPointPaymentAppender;
    private final UserPointReader userPointReader;
    private final UserPointUpdater userPointUpdater;
    private final PortoneAPI portoneAPI;

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
}
