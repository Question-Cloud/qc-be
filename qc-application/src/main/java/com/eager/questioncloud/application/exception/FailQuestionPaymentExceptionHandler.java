package com.eager.questioncloud.application.exception;

import com.eager.questioncloud.core.domain.coupon.UserCoupon;
import com.eager.questioncloud.core.domain.coupon.UserCouponRepository;
import com.eager.questioncloud.core.domain.payment.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.QuestionPaymentRepository;
import com.eager.questioncloud.core.domain.point.UserPoint;
import com.eager.questioncloud.core.domain.point.UserPointRepository;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.exception.FailQuestionPaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class FailQuestionPaymentExceptionHandler {
    private final QuestionPaymentRepository questionPaymentRepository;
    private final UserPointRepository userPointRepository;
    private final UserCouponRepository userCouponRepository;

    @ExceptionHandler(FailQuestionPaymentException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidPaymentException(FailQuestionPaymentException e) {
        QuestionPayment questionPayment = questionPaymentRepository.findByPaymentId(e.getPaymentId());
        questionPayment.fail();
        questionPaymentRepository.save(questionPayment);

        rollbackPoint(questionPayment.getUserId(), questionPayment.getAmount());
        rollbackCoupon(questionPayment.getUserCouponId());

        return ErrorResponse.toResponse(new CustomException(Error.INTERNAL_SERVER_ERROR));
    }

    private void rollbackPoint(Long userId, int amount) {
        UserPoint userPoint = userPointRepository.getUserPoint(userId);
        userPoint.charge(amount);
        userPointRepository.save(userPoint);
    }

    private void rollbackCoupon(Long userCouponId) {
        if (userCouponId == null) {
            return;
        }

        UserCoupon userCoupon = userCouponRepository.getUserCoupon(userCouponId);
        userCoupon.rollback();
        userCouponRepository.save(userCoupon);
    }
}
