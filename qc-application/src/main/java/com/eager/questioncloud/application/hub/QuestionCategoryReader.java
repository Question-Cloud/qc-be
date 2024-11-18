package com.eager.questioncloud.application.hub;

import com.eager.questioncloud.domain.question.QuestionCategoryGroupBySubject;
import com.eager.questioncloud.domain.question.QuestionCategoryGroupBySubject.MainQuestionCategory;
import com.eager.questioncloud.domain.question.QuestionCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionCategoryReader {
    private final QuestionCategoryRepository questionCategoryRepository;

    public List<QuestionCategoryGroupBySubject> getQuestionCategories() {
        List<MainQuestionCategory> mainQuestionCategories = questionCategoryRepository.getMainQuestionCategories();
        return QuestionCategoryGroupBySubject.create(mainQuestionCategories);
    }
}
