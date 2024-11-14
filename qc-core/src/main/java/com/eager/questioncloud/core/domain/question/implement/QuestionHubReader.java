package com.eager.questioncloud.core.domain.question.implement;

import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionHubReader {
    private final QuestionRepository questionRepository;

    public List<QuestionInformation> getQuestions(QuestionFilter questionFilter) {
        return questionRepository.getQuestionInformation(questionFilter);
    }

    public QuestionInformation getQuestion(Long questionId, Long userId) {
        return questionRepository.getQuestionInformation(questionId, userId);
    }

    public int countByQuestionFilter(QuestionFilter questionFilter) {
        return questionRepository.countByQuestionFilter(questionFilter);
    }
}
