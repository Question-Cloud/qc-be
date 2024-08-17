package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionReviewDto.QuestionReviewItem;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface QuestionReviewRepository {
    int getTotal(Long questionId);

    List<QuestionReviewItem> getQuestionReviews(Long questionId, Long userId, Pageable pageable);
}
