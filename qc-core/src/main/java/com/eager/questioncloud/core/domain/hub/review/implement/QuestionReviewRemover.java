package com.eager.questioncloud.core.domain.hub.review.implement;

import com.eager.questioncloud.core.domain.hub.review.dto.QuestionReviewUpdateResult;
import com.eager.questioncloud.core.domain.hub.review.model.QuestionReview;
import com.eager.questioncloud.core.domain.hub.review.repository.QuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionReviewRemover {
    private final QuestionReviewRepository questionReviewRepository;

    //TODO 분산락 로직 추가 시 복구
    @Transactional(isolation = Isolation.READ_COMMITTED)
//    @DistributedLock(key = "'REVIEW:' + #reviewId")
    public QuestionReviewUpdateResult delete(Long reviewId, Long userId) {
        QuestionReview review = questionReviewRepository.getForModifyAndDelete(reviewId, userId);
        review.delete();
        questionReviewRepository.save(review);
        return new QuestionReviewUpdateResult(review.getQuestionId(), review.getRate());
    }
}
