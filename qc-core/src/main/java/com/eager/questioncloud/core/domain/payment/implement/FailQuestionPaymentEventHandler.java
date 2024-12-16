package com.eager.questioncloud.core.domain.payment.implement;

import com.eager.questioncloud.core.domain.coupon.infrastructure.UserCouponRepository;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.event.FailedQuestionPaymentEvent;
import com.eager.questioncloud.core.domain.payment.infrastructure.QuestionPaymentRepository;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.point.infrastructure.UserPointRepository;
import com.eager.questioncloud.core.domain.point.model.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FailQuestionPaymentEventHandler {
    private final QuestionPaymentRepository questionPaymentRepository;
    private final UserPointRepository userPointRepository;
    private final UserCouponRepository userCouponRepository;

    @EventListener
    public void handler(FailedQuestionPaymentEvent event) {
        QuestionPayment questionPayment = event.getQuestionPayment();
        questionPayment.fail();
        questionPaymentRepository.save(questionPayment);

        rollbackPoint(questionPayment.getUserId(), questionPayment.getAmount());
        rollbackCoupon(questionPayment.getQuestionPaymentCoupon().getUserCouponId());
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
