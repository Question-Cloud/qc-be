package com.eager.questioncloud.payment.implement;

import com.eager.questioncloud.payment.model.QuestionPayment;
import com.eager.questioncloud.payment.repository.QuestionPaymentRepository;
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
