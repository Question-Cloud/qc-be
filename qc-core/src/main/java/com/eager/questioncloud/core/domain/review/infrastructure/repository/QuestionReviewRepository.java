package com.eager.questioncloud.core.domain.review.infrastructure.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.review.dto.QuestionReviewDetail;
import com.eager.questioncloud.core.domain.review.model.QuestionReview;
import java.util.List;

public interface QuestionReviewRepository {
    int getTotal(Long questionId);

    List<QuestionReviewDetail> getQuestionReviews(Long questionId, Long userId, PagingInformation pagingInformation);

    QuestionReview getMyQuestionReview(Long questionId, Long userId);

    QuestionReview findByIdAndUserId(Long reviewId, Long userId);

    Boolean isWritten(Long userId, Long questionId);

    QuestionReview save(QuestionReview questionReview);
}
