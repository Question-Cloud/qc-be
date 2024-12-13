package com.eager.questioncloud.core.domain.question.implement;

import com.eager.questioncloud.core.domain.payment.event.CompletedQuestionPaymentEvent;
import com.eager.questioncloud.core.domain.question.infrastructure.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionUpdater {
    private final QuestionRepository questionRepository;

    @EventListener
    @Async
    public void increaseQuestionCount(CompletedQuestionPaymentEvent event) {
        event.getQuestionIds()
            .forEach(questionRepository::increaseQuestionCount);
    }
}
