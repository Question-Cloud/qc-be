package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionDto.QuestionFilterItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReader {
    private final QuestionRepository questionRepository;

    public int getTotalFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels) {
        return questionRepository.getTotalFiltering(questionCategoryIds, questionLevels);
    }

    public List<QuestionFilterItem> getQuestionListByFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels,
        QuestionSortType sort) {
        return questionRepository.getQuestionListByFiltering(questionCategoryIds, questionLevels, sort);
    }
}
