package com.eager.questioncloud.core.domain.question.service;

import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject;
import com.eager.questioncloud.core.domain.question.implement.QuestionCategoryReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionCategoryService {
    private final QuestionCategoryReader questionCategoryReader;

    public List<QuestionCategoryGroupBySubject> getQuestionCategories() {
        return questionCategoryReader.getQuestionCategories();
    }
}
