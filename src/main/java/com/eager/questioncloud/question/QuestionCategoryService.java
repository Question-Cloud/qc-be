package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionCategoryDto.QuestionCategoryListItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionCategoryService {
    private final QuestionCategoryReader questionCategoryReader;

    public List<QuestionCategoryListItem> getQuestionCategories() {
        return QuestionCategoryListItem.groupBy(questionCategoryReader.getQuestionCategories());
    }
}
