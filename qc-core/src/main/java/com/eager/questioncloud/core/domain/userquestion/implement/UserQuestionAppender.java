package com.eager.questioncloud.core.domain.userquestion.implement;

import com.eager.questioncloud.core.domain.payment.event.CompletedQuestionPaymentEvent;
import com.eager.questioncloud.core.domain.userquestion.infrastructure.UserQuestionRepository;
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion;
import com.eager.questioncloud.exception.FailQuestionPaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionAppender {
    private final UserQuestionRepository userQuestionRepository;

    @EventListener
    public void appendUserQuestion(CompletedQuestionPaymentEvent event) {
        try {
            userQuestionRepository.saveAll(UserQuestion.create(event.getQuestionPayment().getUserId(), event.getQuestionIds()));
        } catch (Exception e) {
            throw new FailQuestionPaymentException(event.getQuestionPayment().getOrderId());
        }
    }
}
