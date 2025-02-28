package com.eager.questioncloud.application.business.payment.question.service;

import com.eager.questioncloud.application.business.payment.question.implement.QuestionOrderGenerator;
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionOrderService {
    private final QuestionOrderGenerator questionOrderGenerator;

    public QuestionOrder generateQuestionOrder(Long userId, List<Long> questionIds) {
        return questionOrderGenerator.generateQuestionOrder(userId, questionIds);
    }
}
