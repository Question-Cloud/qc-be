package com.eager.questioncloud.core.domain.hub.payment.implement;

import com.eager.questioncloud.core.domain.hub.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.hub.payment.repository.QuestionPaymentRepository;
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
