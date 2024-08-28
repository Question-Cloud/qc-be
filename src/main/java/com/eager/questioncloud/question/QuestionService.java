package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionDto.QuestionDetail;
import com.eager.questioncloud.question.QuestionDto.QuestionFilterItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionReader questionReader;

    public int getTotalFiltering(QuestionFilter questionFilter) {
        return questionReader.getTotalFiltering(questionFilter);
    }

    public List<QuestionFilterItem> getQuestionListByFiltering(QuestionFilter questionFilter) {
        return questionReader.getQuestionListByFiltering(questionFilter);
    }

    public QuestionDetail getQuestionDetail(Long questionId, Long userId) {
        return questionReader.getQuestionDetail(questionId, userId);
    }
}
