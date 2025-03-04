package com.eager.questioncloud.application.business.payment.question.implement;

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent;
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentHistoryRepository;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory;
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentHistoryRegister {
    private final QuestionRepository questionRepository;
    private final QuestionPaymentHistoryRepository questionPaymentHistoryRepository;

    @EventListener
    public void saveQuestionPaymentHistory(QuestionPaymentEvent event) {
        List<QuestionInformation> questions = questionRepository.findByQuestionIdIn(event.getQuestionPayment().getOrder().getQuestionIds());
        questionPaymentHistoryRepository.save(QuestionPaymentHistory.create(event.getQuestionPayment(), questions));
    }
}
