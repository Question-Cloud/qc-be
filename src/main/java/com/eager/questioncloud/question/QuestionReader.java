package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionDto.QuestionDetail;
import com.eager.questioncloud.question.QuestionDto.QuestionFilterItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReader {
    private final QuestionRepository questionRepository;

    public int getTotalFiltering(QuestionFilter questionFilter) {
        return questionRepository.getTotalFiltering(questionFilter);
    }

    public List<QuestionFilterItem> getQuestionListByFiltering(QuestionFilter questionFilter) {
        return questionRepository.getQuestionListByFiltering(questionFilter);
    }

    public QuestionDetail getQuestionDetail(Long questionId, Long userId) {
        return questionRepository.getQuestionDetail(questionId, userId);
    }

    public List<Question> getQuestions(List<Long> questionIds) {
        return questionRepository.getQuestionListInIds(questionIds);
    }
}
