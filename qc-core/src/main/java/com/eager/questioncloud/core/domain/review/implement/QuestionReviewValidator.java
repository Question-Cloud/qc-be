package com.eager.questioncloud.core.domain.review.implement;

import com.eager.questioncloud.core.domain.library.implement.UserQuestionLibraryReader;
import com.eager.questioncloud.core.domain.question.implement.QuestionReader;
import com.eager.questioncloud.core.domain.review.model.QuestionReview;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//TODO 분산락 로직 추가 시 복구
@Component
@RequiredArgsConstructor
public class QuestionReviewValidator {
    private final QuestionReviewReader questionReviewReader;
    private final UserQuestionLibraryReader userQuestionLibraryReader;
    private final QuestionReader questionReader;

    //    @DistributedLock(key = "'REVIEW-APPEND:' + #questionReview.getReviewerId()")
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
