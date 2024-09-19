package com.eager.questioncloud.question;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.question.QuestionDto.QuestionInformation;
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
}
