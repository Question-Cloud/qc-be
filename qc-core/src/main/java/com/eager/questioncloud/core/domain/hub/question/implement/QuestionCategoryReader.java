package com.eager.questioncloud.core.domain.hub.question.implement;

import com.eager.questioncloud.core.domain.hub.question.dto.QuestionCategoryDto.QuestionCategoryItem;
import com.eager.questioncloud.core.domain.hub.question.repository.QuestionCategoryRepository;
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
