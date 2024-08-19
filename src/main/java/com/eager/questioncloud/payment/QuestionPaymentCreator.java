package com.eager.questioncloud.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentCreator {
    private final QuestionPaymentRepository questionPaymentRepository;

    public QuestionPayment append(QuestionPayment questionPayment) {
        return questionPaymentRepository.append(questionPayment);
    }
}
