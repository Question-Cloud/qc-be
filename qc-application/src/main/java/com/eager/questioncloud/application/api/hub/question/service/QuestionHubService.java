package com.eager.questioncloud.application.api.hub.question.service;

import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject;
import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject.MainQuestionCategory;
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.infrastructure.QuestionCategoryRepository;
import com.eager.questioncloud.core.domain.question.infrastructure.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionHubService {
    private final QuestionRepository questionRepository;
    private final QuestionCategoryRepository questionCategoryRepository;

    public int getTotalFiltering(QuestionFilter questionFilter) {
        return questionRepository.countByQuestionFilter(questionFilter);
    }

    public List<QuestionInformation> getQuestionListByFiltering(QuestionFilter questionFilter) {
        return questionRepository.getQuestionInformation(questionFilter);
    }

    public QuestionInformation getQuestionInformation(Long questionId, Long userId) {
        return questionRepository.getQuestionInformation(questionId, userId);
    }

    public List<QuestionCategoryGroupBySubject> getQuestionCategories() {
        List<MainQuestionCategory> mainQuestionCategories = questionCategoryRepository.getMainQuestionCategories();
        return QuestionCategoryGroupBySubject.create(mainQuestionCategories);
    }
}
