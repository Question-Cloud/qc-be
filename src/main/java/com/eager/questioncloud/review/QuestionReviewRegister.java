package com.eager.questioncloud.review;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.library.UserQuestionLibraryReader;
import com.eager.questioncloud.question.QuestionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewRegister {
    private final QuestionReviewReader questionReviewReader;
    private final QuestionReviewAppender questionReviewAppender;
    private final UserQuestionLibraryReader userQuestionLibraryReader;
    private final QuestionReader questionReader;

    public QuestionReview register(QuestionReview questionReview) {
        if (!questionReader.isAvailable(questionReview.getQuestionId())) {
            throw new CustomException(Error.NOT_FOUND);
        }

        if (!userQuestionLibraryReader.isOwned(questionReview.getReviewerId(), questionReview.getQuestionId())) {
            throw new CustomException(Error.NOT_OWNED_QUESTION);
        }

        if (questionReviewReader.isWritten(questionReview.getQuestionId(), questionReview.getReviewerId())) {
            throw new CustomException(Error.ALREADY_REGISTER_REVIEW);
        }
        return questionReviewAppender.append(questionReview);
    }
}
