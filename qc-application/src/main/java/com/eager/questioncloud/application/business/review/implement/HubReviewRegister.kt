package com.eager.questioncloud.application.business.review.implement;

import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewRepository;
import com.eager.questioncloud.core.domain.review.model.QuestionReview;
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubReviewRegister {
    private final QuestionReviewRepository questionReviewRepository;
    private final QuestionRepository questionRepository;
    private final UserQuestionRepository userQuestionRepository;

    public void register(QuestionReview questionReview) {
        try {
            if (isUnAvailableQuestion(questionReview.getQuestionId())) {
                throw new CoreException(Error.UNAVAILABLE_QUESTION);
            }

            if (isNotOwnedQuestion(questionReview.getReviewerId(), questionReview.getQuestionId())) {
                throw new CoreException(Error.NOT_OWNED_QUESTION);
            }

            if (isAlreadyWrittenReview(questionReview.getReviewerId(), questionReview.getQuestionId())) {
                throw new CoreException(Error.ALREADY_REGISTER_REVIEW);
            }
            questionReviewRepository.save(questionReview);
        } catch (DataIntegrityViolationException e) {
            throw new CoreException(Error.ALREADY_REGISTER_REVIEW);
        }
    }

    private Boolean isUnAvailableQuestion(Long questionId) {
        return !questionRepository.isAvailable(questionId);
    }

    private Boolean isNotOwnedQuestion(Long userId, Long questionId) {
        return !userQuestionRepository.isOwned(userId, questionId);
    }

    private Boolean isAlreadyWrittenReview(Long userId, Long questionId) {
        return questionReviewRepository.isWritten(userId, questionId);
    }
}
