package com.eager.questioncloud.core.domain.payment.question.implement;

import com.eager.questioncloud.core.domain.payment.point.implement.UserPointManager;
import com.eager.questioncloud.core.domain.payment.question.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.question.repository.QuestionPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionPaymentFailHandler {
    private final QuestionPaymentRepository questionPaymentRepository;
    private final UserPointManager userPointManager;

    @Transactional
    public void failHandler(QuestionPayment questionPayment) {
        questionPayment.fail();
        questionPaymentRepository.save(questionPayment);
        
        userPointManager.chargePoint(questionPayment.getUserId(), questionPayment.getAmount());
    }
}
