package com.eager.questioncloud.core.domain.review;

import com.eager.questioncloud.core.common.PagingInformation;
import java.util.List;

public interface QuestionReviewRepository {
    int getTotal(Long questionId);

    List<QuestionReviewDetail> getQuestionReviews(Long questionId, Long userId, PagingInformation pagingInformation);

    QuestionReview getMyQuestionReview(Long questionId, Long userId);

    QuestionReview findByIdAndUserId(Long reviewId, Long userId);

    Boolean isWritten(Long userId, Long questionId);

    QuestionReview save(QuestionReview questionReview);
}
