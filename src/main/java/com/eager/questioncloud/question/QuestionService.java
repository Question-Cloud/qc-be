package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionDto.QuestionDetail;
import com.eager.questioncloud.question.QuestionDto.QuestionFilterItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionReader questionReader;

    public int getTotalFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels) {
        return questionReader.getTotalFiltering(questionCategoryIds, questionLevels);
    }

    public List<QuestionFilterItem> getQuestionListByFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels,
        QuestionSortType sort, Pageable pageable) {
        return questionReader.getQuestionListByFiltering(questionCategoryIds, questionLevels, sort, pageable);
    }

    public QuestionDetail getQuestionDetail(Long questionId) {
        return questionReader.getQuestionDetail(questionId);
    }
}
