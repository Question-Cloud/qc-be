package com.eager.questioncloud.application.hub;

import com.eager.questioncloud.domain.question.QuestionCategoryGroupBySubject;
import com.eager.questioncloud.domain.question.QuestionFilter;
import com.eager.questioncloud.domain.question.QuestionInformation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionHubService {
    private final QuestionHubReader questionHubReader;
    private final QuestionCategoryReader questionCategoryReader;

    public int getTotalFiltering(QuestionFilter questionFilter) {
        return questionHubReader.countByQuestionFilter(questionFilter);
    }

    public List<QuestionInformation> getQuestionListByFiltering(QuestionFilter questionFilter) {
        return questionHubReader.getQuestions(questionFilter);
    }

    public QuestionInformation getQuestionInformation(Long questionId, Long userId) {
        return questionHubReader.getQuestion(questionId, userId);
    }

    public List<QuestionCategoryGroupBySubject> getQuestionCategories() {
        return questionCategoryReader.getQuestionCategories();
    }
}
