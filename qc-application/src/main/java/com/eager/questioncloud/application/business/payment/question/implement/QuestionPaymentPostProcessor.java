package com.eager.questioncloud.application.business.payment.question.implement;

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent;
import com.eager.questioncloud.application.message.FailQuestionPaymentMessage;
import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.message.MessageType;
import com.eager.questioncloud.core.domain.userquestion.implement.UserQuestionAppender;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentPostProcessor {
    private final UserQuestionAppender userQuestionAppender;
    private final MessageSender messageSender;


    @EventListener
    public void appendUserQuestion(QuestionPaymentEvent event) {
        try {
            userQuestionAppender.appendUserQuestion(event.getQuestionPayment().getUserId(), event.getQuestionPayment().getOrder().getQuestionIds());
        } catch (Exception e) {
            messageSender.sendMessage(MessageType.FAIL_QUESTION_PAYMENT, new FailQuestionPaymentMessage(event.getQuestionPayment()));
            throw new CoreException(Error.PAYMENT_ERROR);
        }
    }
}
