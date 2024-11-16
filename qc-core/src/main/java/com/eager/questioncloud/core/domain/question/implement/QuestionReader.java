package com.eager.questioncloud.core.domain.question.implement;

import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.repository.QuestionRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReader {
    private final QuestionRepository questionRepository;

    public List<Question> getQuestions(List<Long> questionIds) {
        List<Question> questions = questionRepository.getQuestionsByQuestionIds(questionIds);

        if (questions.size() != questionIds.size()) {
            throw new CustomException(Error.UNAVAILABLE_QUESTION);
        }

        return questions;
    }

    public Question getQuestion(Long questionId) {
        return questionRepository.get(questionId);
    }

    public Boolean isAvailable(Long questionId) {
        return questionRepository.isAvailable(questionId);
    }
}
