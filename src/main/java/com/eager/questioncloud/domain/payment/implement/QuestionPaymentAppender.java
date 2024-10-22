package com.eager.questioncloud.domain.payment.implement;

import com.eager.questioncloud.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.domain.payment.repository.QuestionPaymentRepository;
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
