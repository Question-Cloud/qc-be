package com.eager.questioncloud.core.domain.payment.implement;

import com.eager.questioncloud.core.domain.payment.event.CompletedQuestionPaymentEvent;
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentHistoryRepository;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory;
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentHistoryGenerator {
    private final QuestionRepository questionRepository;
    private final QuestionPaymentHistoryRepository questionPaymentHistoryRepository;

    @EventListener
    @Async
    public void saveQuestionPaymentHistory(CompletedQuestionPaymentEvent event) {
        List<QuestionInformation> questions = questionRepository.findByQuestionIdIn(event.getQuestionIds());
        questionPaymentHistoryRepository.save(QuestionPaymentHistory.create(event.getQuestionPayment(), questions));
    }
}
