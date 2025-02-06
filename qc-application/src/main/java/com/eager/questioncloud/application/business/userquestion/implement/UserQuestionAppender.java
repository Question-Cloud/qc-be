package com.eager.questioncloud.application.business.userquestion.implement;

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent;
import com.eager.questioncloud.application.message.FailQuestionPaymentMessage;
import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.message.MessageType;
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository;
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionAppender {
    private final UserQuestionRepository userQuestionRepository;
    private final MessageSender messageSender;

    @EventListener
    public void appendUserQuestion(QuestionPaymentEvent event) {
        try {
            userQuestionRepository.saveAll(
                UserQuestion.create(
                    event.getQuestionPayment().getUserId(),
                    event.getQuestionPayment().getOrder().getQuestionIds()
                )
            );
        } catch (Exception e) {
            messageSender.sendDelayMessage(MessageType.FAIL_QUESTION_PAYMENT, FailQuestionPaymentMessage.create(event.getQuestionPayment()), 0);
            throw new CoreException(Error.PAYMENT_ERROR);
        }
    }
}
