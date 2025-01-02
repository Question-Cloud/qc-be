package com.eager.questioncloud.application.api.payment.question.implement;

import com.eager.questioncloud.application.message.FailQuestionPaymentMessage;
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentRepository;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository;
import com.eager.questioncloud.core.domain.point.model.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FailQuestionPaymentEventHandler {
    private final QuestionPaymentRepository questionPaymentRepository;
    private final UserPointRepository userPointRepository;
    private final UserCouponRepository userCouponRepository;

    @RabbitListener(queues = "fail-question-payment")
    public void handler(FailQuestionPaymentMessage message) {
        QuestionPayment questionPayment = message.getQuestionPayment();
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
