package com.eager.questioncloud.domain.question.implement;

import com.eager.questioncloud.domain.question.dto.QuestionCategoryDto.QuestionCategoryItem;
import com.eager.questioncloud.domain.question.repository.QuestionCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionCategoryReader {
    private final QuestionCategoryRepository questionCategoryRepository;

    public List<QuestionCategoryItem> getQuestionCategories() {
        return questionCategoryRepository.getQuestionCategories();
    }
}
