package com.eager.questioncloud.application.business.payment.question.implement;

import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentHistoryRepository;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory;
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentHistoryGenerator {
    private final QuestionRepository questionRepository;
    private final QuestionPaymentHistoryRepository questionPaymentHistoryRepository;

    public void saveQuestionPaymentHistory(QuestionPayment questionPayment) {
        List<QuestionInformation> questions = questionRepository.findByQuestionIdIn(questionPayment.getOrder().getQuestionIds());
        questionPaymentHistoryRepository.save(QuestionPaymentHistory.create(questionPayment, questions));
    }
}
