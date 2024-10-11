package com.eager.questioncloud.review.implement;

import com.eager.questioncloud.annotation.DistributedLock;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.library.implement.UserQuestionLibraryReader;
import com.eager.questioncloud.question.implement.QuestionReader;
import com.eager.questioncloud.review.domain.QuestionReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewValidator {
    private final QuestionReviewReader questionReviewReader;
    private final UserQuestionLibraryReader userQuestionLibraryReader;
    private final QuestionReader questionReader;

    @DistributedLock(key = "'REVIEW-APPEND:' + #questionReview.getReviewerId()")
    public void validate(QuestionReview questionReview) {
        if (!questionReader.isAvailable(questionReview.getQuestionId())) {
            throw new CustomException(Error.NOT_FOUND);
        }

        if (!userQuestionLibraryReader.isOwned(questionReview.getReviewerId(), questionReview.getQuestionId())) {
            throw new CustomException(Error.NOT_OWNED_QUESTION);
        }

        if (questionReviewReader.isWritten(questionReview.getQuestionId(), questionReview.getReviewerId())) {
            throw new CustomException(Error.ALREADY_REGISTER_REVIEW);
        }
    }
}