package com.eager.questioncloud.application.api.payment.question.implement;

import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentOrder;
import com.eager.questioncloud.core.domain.question.infrastructure.QuestionRepository;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.userquestion.infrastructure.UserQuestionRepository;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionOrderGenerator {
    private final UserQuestionRepository userQuestionRepository;
    private final QuestionRepository questionRepository;

    public QuestionPaymentOrder generateQuestionPaymentOrder(Long userId, List<Long> questionIds) {
        if (checkAlreadyOwned(userId, questionIds)) {
            throw new CustomException(Error.ALREADY_OWN_QUESTION);
        }

        List<Question> questions = getQuestions(questionIds);
        return QuestionPaymentOrder.createOrder(questions);
    }

    private Boolean checkAlreadyOwned(Long userId, List<Long> questionIds) {
        return userQuestionRepository.isOwned(userId, questionIds);
    }

    private List<Question> getQuestions(List<Long> questionIds) {
        List<Question> questions = questionRepository.getQuestionsByQuestionIds(questionIds);
        if (questions.size() != questionIds.size()) {
            throw new CustomException(Error.UNAVAILABLE_QUESTION);
        }
        return questions;
    }
}
