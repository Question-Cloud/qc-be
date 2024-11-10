package com.eager.questioncloud.core.domain.payment.question.implement;

import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.coupon.repository.UserCouponRepository;
import com.eager.questioncloud.core.domain.payment.question.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.question.repository.QuestionPaymentRepository;
import com.eager.questioncloud.core.domain.point.implement.UserPointManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionPaymentFailHandler {
    private final QuestionPaymentRepository questionPaymentRepository;
    private final UserPointManager userPointManager;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    public void failHandler(QuestionPayment questionPayment) {
        questionPayment.fail();
        questionPaymentRepository.save(questionPayment);

        rollbackPoint(questionPayment.getUserId(), questionPayment.getAmount());
        rollbackCoupon(questionPayment.getUserCouponId());
    }

    private void rollbackPoint(Long userId, int amount) {
        userPointManager.chargePoint(userId, amount);
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
