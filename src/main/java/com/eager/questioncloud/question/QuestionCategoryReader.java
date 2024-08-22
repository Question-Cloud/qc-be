package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionCategoryDto.QuestionCategoryItem;
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
