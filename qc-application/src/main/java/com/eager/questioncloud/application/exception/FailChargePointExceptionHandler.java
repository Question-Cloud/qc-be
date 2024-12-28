package com.eager.questioncloud.application.exception;

import com.eager.questioncloud.application.api.payment.point.implement.PGAPI;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.exception.FailChargePointException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse
                    .builder()
                    .status(500)
                    .message("포인트 충전 중 오류가 발생하였습니다.")
                    .build()
            );
    }
}
