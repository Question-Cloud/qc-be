package com.eager.questioncloud.core.domain.questionhub.question.implement;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.questionhub.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.questionhub.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.core.domain.questionhub.question.model.Question;
import com.eager.questioncloud.core.domain.questionhub.question.repository.QuestionRepository;
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
        return questionRepository.getTotalFiltering(questionFilter);
    }

    public List<QuestionInformation> getQuestionListByFiltering(QuestionFilter questionFilter) {
        return questionRepository.getQuestionListByFiltering(questionFilter);
    }

    public QuestionInformation getQuestionInformation(Long questionId, Long userId) {
        return questionRepository.getQuestionInformation(questionId, userId);
    }

    public List<Question> getQuestions(List<Long> questionIds) {
        List<Question> questions = questionRepository.getQuestionListInIds(questionIds);

        if (questions.size() != questionIds.size()) {
            throw new CustomException(Error.UNAVAILABLE_QUESTION);
        }

        return questions;
    }

    public Boolean isAvailable(Long questionId) {
        return questionRepository.isAvailable(questionId);
    }

    public Question findByIdAndCreatorId(Long questionId, Long creatorId) {
        return questionRepository.findByIdAndCreatorId(questionId, creatorId);
    }

    public List<QuestionInformation> getCreatorQuestions(Long creatorId, PagingInformation pagingInformation) {
        return questionRepository.getCreatorQuestions(creatorId, pagingInformation);
    }

    public int countCreatorQuestion(Long creatorId) {
        return questionRepository.countCreatorQuestion(creatorId);
    }
}
