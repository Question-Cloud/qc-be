package com.eager.questioncloud.application.business.payment.question.service;

import com.eager.questioncloud.application.business.payment.question.implement.QuestionPaymentCouponReader;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionPaymentCouponService {
    private final QuestionPaymentCouponReader questionPaymentCouponReader;

    public QuestionPaymentCoupon getQuestionPaymentCoupon(Long userCouponId, Long userId) {
        return questionPaymentCouponReader.getQuestionPaymentCoupon(userCouponId, userId);
    }
}
