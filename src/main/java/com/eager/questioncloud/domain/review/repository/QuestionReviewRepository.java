package com.eager.questioncloud.domain.review.repository;

import com.eager.questioncloud.domain.review.model.QuestionReview;
import com.eager.questioncloud.domain.review.dto.QuestionReviewDto.QuestionReviewItem;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface QuestionReviewRepository {
    int getTotal(Long questionId);

    List<QuestionReviewItem> getQuestionReviews(Long questionId, Long userId, Pageable pageable);

    QuestionReview getMyQuestionReview(Long questionId, Long userId);

    QuestionReview getForModifyAndDelete(Long reviewId, Long userId);

    Boolean isWritten(Long questionId, Long userId);

    QuestionReview save(QuestionReview questionReview);
}
