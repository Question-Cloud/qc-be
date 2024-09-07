package com.eager.questioncloud.review;

import com.eager.questioncloud.review.QuestionReviewDto.QuestionReviewItem;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface QuestionReviewRepository {
    int getTotal(Long questionId);

    List<QuestionReviewItem> getQuestionReviews(Long questionId, Long userId, Pageable pageable);

    QuestionReview getMyQuestionReview(Long questionId, Long userId);

    QuestionReview append(QuestionReview questionReview);

    Boolean isWritten(Long questionId, Long userId);
}
