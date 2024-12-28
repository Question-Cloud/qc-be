package com.eager.questioncloud.core.domain.userquestion.implement;

import com.eager.questioncloud.core.domain.payment.event.CompletedQuestionPaymentEvent;
import com.eager.questioncloud.core.domain.payment.event.FailedQuestionPaymentEvent;
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository;
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionAppender {
    private final UserQuestionRepository userQuestionRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public void appendUserQuestion(CompletedQuestionPaymentEvent event) {
        try {
            userQuestionRepository.saveAll(UserQuestion.create(event.getQuestionPayment().getUserId(), event.getQuestionIds()));
        } catch (Exception e) {
            applicationEventPublisher.publishEvent(new FailedQuestionPaymentEvent(event.getQuestionPayment()));
        }
    }
}
