package com.eager.questioncloud.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentAppender {
    private final QuestionPaymentRepository questionPaymentRepository;

    public QuestionPayment createQuestionPayment(QuestionPayment questionPayment) {
        return questionPaymentRepository.save(questionPayment);
    }
}
