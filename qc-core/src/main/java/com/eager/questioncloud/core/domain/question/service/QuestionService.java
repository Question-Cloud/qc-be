package com.eager.questioncloud.core.domain.question.service;

import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.implement.QuestionReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionReader questionReader;

    public int getTotalFiltering(QuestionFilter questionFilter) {
        return questionReader.getTotalFiltering(questionFilter);
    }

    public List<QuestionInformation> getQuestionListByFiltering(QuestionFilter questionFilter) {
        return questionReader.getQuestionListByFiltering(questionFilter);
    }

    public QuestionInformation getQuestionInformation(Long questionId, Long userId) {
        return questionReader.getQuestionInformation(questionId, userId);
    }
}
