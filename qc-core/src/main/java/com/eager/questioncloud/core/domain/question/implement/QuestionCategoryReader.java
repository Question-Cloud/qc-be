package com.eager.questioncloud.core.domain.question.implement;

import com.eager.questioncloud.core.domain.question.dto.MainQuestionCategoryList;
import com.eager.questioncloud.core.domain.question.dto.MainQuestionCategoryList.MainQuestionCategory;
import com.eager.questioncloud.core.domain.question.repository.QuestionCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionCategoryReader {
    private final QuestionCategoryRepository questionCategoryRepository;

    public List<MainQuestionCategoryList> getQuestionCategories() {
        List<MainQuestionCategory> mainQuestionCategories = questionCategoryRepository.getMainQuestionCategories();
        return MainQuestionCategoryList.create(mainQuestionCategories);
    }
}
