package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionReviewDto.QuestionReviewItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionReviewService {
    private final QuestionReviewReader questionReviewReader;

    public int getTotal(Long questionId) {
        return questionReviewReader.getTotal(questionId);
    }

    public List<QuestionReviewItem> getQuestionReviews(Long questionId, Long userId, Pageable pageable) {
        return questionReviewReader.getQuestionReviews(questionId, userId, pageable);
    }
}
