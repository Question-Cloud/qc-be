package com.eager.questioncloud.application.api.payment.question.implement;

import com.eager.questioncloud.core.domain.payment.model.QuestionOrder;
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.userquestion.infrastructure.UserQuestionRepository;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionOrderGenerator {
    private final UserQuestionRepository userQuestionRepository;
    private final QuestionRepository questionRepository;

    public QuestionOrder generateQuestionOrder(Long userId, List<Long> questionIds) {
        if (checkAlreadyOwned(userId, questionIds)) {
            throw new CoreException(Error.ALREADY_OWN_QUESTION);
        }

        List<Question> questions = getQuestions(questionIds);
        return QuestionOrder.createOrder(questions);
    }

    private Boolean checkAlreadyOwned(Long userId, List<Long> questionIds) {
        return userQuestionRepository.isOwned(userId, questionIds);
    }

    private List<Question> getQuestions(List<Long> questionIds) {
        List<Question> questions = questionRepository.getQuestionsByQuestionIds(questionIds);
        if (questions.size() != questionIds.size()) {
            throw new CoreException(Error.UNAVAILABLE_QUESTION);
        }
        return questions;
    }
}
