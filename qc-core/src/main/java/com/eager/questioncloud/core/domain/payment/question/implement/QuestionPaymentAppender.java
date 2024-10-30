package com.eager.questioncloud.core.domain.payment.question.implement;

import com.eager.questioncloud.core.domain.payment.question.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.question.repository.QuestionPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentAppender {
    private final QuestionPaymentRepository questionPaymentRepository;

    public QuestionPayment append(QuestionPayment questionPayment) {
        return questionPaymentRepository.save(questionPayment);
    }
}
