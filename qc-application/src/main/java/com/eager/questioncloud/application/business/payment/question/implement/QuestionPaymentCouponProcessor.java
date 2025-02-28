package com.eager.questioncloud.application.business.payment.question.implement;

import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentCouponProcessor {
    private final UserCouponRepository userCouponRepository;

    public void applyCoupon(QuestionPayment questionPayment) {
        if (!questionPayment.checkUsingCoupon()) {
            return;
        }

        questionPayment.useCoupon();

        if (!userCouponRepository.use(questionPayment.getQuestionPaymentCoupon().getUserCouponId())) {
            throw new CoreException(Error.FAIL_USE_COUPON);
        }
    }
}
