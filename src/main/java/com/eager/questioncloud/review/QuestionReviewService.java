package com.eager.questioncloud.review;

import com.eager.questioncloud.review.QuestionReviewDto.QuestionReviewItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionReviewService {
    private final QuestionReviewReader questionReviewReader;
    private final QuestionReviewAppender questionReviewAppender;
    private final QuestionReviewUpdater questionReviewUpdater;
    private final QuestionReviewRemover questionReviewRemover;

    public int getTotal(Long questionId) {
        return questionReviewReader.getTotal(questionId);
    }

    public List<QuestionReviewItem> getQuestionReviews(Long questionId, Long userId, Pageable pageable) {
        return questionReviewReader.getQuestionReviews(questionId, userId, pageable);
    }

    public QuestionReview getMyQuestionReview(Long questionId, Long userId) {
        return questionReviewReader.getMyQuestionReview(questionId, userId);
    }

    public QuestionReview register(QuestionReview questionReview) {
        questionReviewAppender.append(questionReview);
        return questionReview;
    }

    public void modify(Long reviewId, Long userId, String comment, int rate) {
        questionReviewUpdater.update(reviewId, userId, comment, rate);
    }

    public void delete(Long reviewId, Long userId) {
        questionReviewRemover.delete(reviewId, userId);
    }
}
