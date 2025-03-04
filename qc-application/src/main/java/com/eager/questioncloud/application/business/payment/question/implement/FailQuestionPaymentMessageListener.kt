package com.eager.questioncloud.application.business.payment.question.implement;

import com.eager.questioncloud.application.message.FailQuestionPaymentMessage;
import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.message.MessageType;
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentRepository;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository;
import com.eager.questioncloud.core.domain.point.model.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FailQuestionPaymentMessageListener {
    private final QuestionPaymentRepository questionPaymentRepository;
    private final UserPointRepository userPointRepository;
    private final UserCouponRepository userCouponRepository;
    private final MessageSender messageSender;

    @RabbitListener(id = "fail-question-payment", queues = "fail-question-payment")
    @Transactional
    public void handler(FailQuestionPaymentMessage message) {
        try {
            QuestionPayment questionPayment = message.getQuestionPayment();
            questionPayment.fail();
            questionPaymentRepository.save(questionPayment);

            rollbackPoint(questionPayment.getUserId(), questionPayment.getAmount());
            rollbackCoupon(questionPayment.getQuestionPaymentCoupon().getUserCouponId());
        } catch (Exception e) {
            message.increaseFailCount();
            messageSender.sendDelayMessage(MessageType.FAIL_QUESTION_PAYMENT, message, message.getFailCount());
            throw new AmqpRejectAndDontRequeueException(e);
        }
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
