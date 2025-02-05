package com.eager.questioncloud.application.business.question.implement;

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent;
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionStatisticsUpdater {
    private final QuestionRepository questionRepository;

    @EventListener
    public void updateSalesCount(QuestionPaymentEvent event) {
        event.getQuestionPayment().getOrder().getItems()
            .forEach(item -> questionRepository.increaseQuestionCount(item.getQuestionId()));
    }
}
