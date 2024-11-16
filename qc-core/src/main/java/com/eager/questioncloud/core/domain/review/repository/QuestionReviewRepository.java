package com.eager.questioncloud.core.domain.review.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.review.dto.QuestionReviewDetail;
import com.eager.questioncloud.core.domain.review.model.QuestionReview;
import java.util.List;

public interface QuestionReviewRepository {
    int getTotal(Long questionId);

    List<QuestionReviewDetail> getQuestionReviews(Long questionId, Long userId, PagingInformation pagingInformation);

    QuestionReview getMyQuestionReview(Long questionId, Long userId);

    QuestionReview findByIdAndUserId(Long reviewId, Long userId);

    Boolean isWritten(Long questionId, Long userId);

    QuestionReview save(QuestionReview questionReview);
}
