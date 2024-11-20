package com.eager.questioncloud.application.exception;

import com.eager.questioncloud.application.api.payment.PGAPI;
import com.eager.questioncloud.domain.point.ChargePointPayment;
import com.eager.questioncloud.domain.point.ChargePointPaymentRepository;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.exception.FailChargePointException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class FailChargePointExceptionHandler {
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final PGAPI pgAPI;

    @ExceptionHandler(FailChargePointException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidPaymentException(FailChargePointException e) {
        ChargePointPayment chargePointPayment = chargePointPaymentRepository.findByPaymentId(e.getPaymentId());
        chargePointPayment.fail();
        chargePointPaymentRepository.save(chargePointPayment);

        pgAPI.cancel(chargePointPayment.getPaymentId());

        return ErrorResponse.toResponse(new CustomException(Error.INTERNAL_SERVER_ERROR));
    }
}
