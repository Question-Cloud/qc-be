package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionDto.QuestionDetail;
import com.eager.questioncloud.question.QuestionDto.QuestionFilterItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReader {
    private final QuestionRepository questionRepository;

    public int getTotalFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels, QuestionType questionType) {
        return questionRepository.getTotalFiltering(questionCategoryIds, questionLevels, questionType);
    }

    public List<QuestionFilterItem> getQuestionListByFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels,
        QuestionType questionType, QuestionSortType sort, Pageable pageable) {
        return questionRepository.getQuestionListByFiltering(questionCategoryIds, questionLevels, questionType, sort, pageable);
    }

    public QuestionDetail getQuestionDetail(Long questionId) {
        return questionRepository.getQuestionDetail(questionId);
    }

    public List<Question> getQuestions(List<Long> questionIds) {
        return questionRepository.getQuestionListInIds(questionIds);
    }
}
