package com.eager.questioncloud.core.domain.question.implement;

import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformation;
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

    public int getTotalFiltering(QuestionFilter questionFilter) {
        return questionRepository.countByQuestionFilter(questionFilter);
    }

    public List<QuestionInformation> getQuestionListByFiltering(QuestionFilter questionFilter) {
        return questionRepository.getQuestionInformation(questionFilter);
    }

    public QuestionInformation getQuestionInformation(Long questionId, Long userId) {
        return questionRepository.getQuestionInformation(questionId, userId);
    }

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
