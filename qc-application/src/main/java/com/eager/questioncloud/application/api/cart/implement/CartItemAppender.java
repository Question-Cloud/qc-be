package com.eager.questioncloud.application.api.cart.implement;

import com.eager.questioncloud.core.domain.cart.infrastructure.CartItemRepository;
import com.eager.questioncloud.core.domain.cart.model.CartItem;
import com.eager.questioncloud.core.domain.question.infrastructure.QuestionRepository;
import com.eager.questioncloud.core.domain.userquestion.infrastructure.UserQuestionRepository;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartItemAppender {
    private final QuestionRepository questionRepository;
    private final CartItemRepository cartItemRepository;
    private final UserQuestionRepository userQuestionRepository;

    public void append(Long userId, Long questionId) {
        if (isUnAvailableQuestion(questionId)) {
            throw new CustomException(Error.UNAVAILABLE_QUESTION);
        }

        if (isAlreadyInCart(userId, questionId)) {
            throw new CustomException(Error.ALREADY_IN_CART);
        }

        if (isAlreadyOwned(userId, questionId)) {
            throw new CustomException(Error.ALREADY_OWN_QUESTION);
        }

        cartItemRepository.save(CartItem.create(userId, questionId));
    }

    private Boolean isUnAvailableQuestion(Long questionId) {
        return !questionRepository.isAvailable(questionId);
    }

    private Boolean isAlreadyInCart(Long userId, Long questionId) {
        return cartItemRepository.isExistsInCart(userId, questionId);
    }

    private Boolean isAlreadyOwned(Long userId, Long questionId) {
        return userQuestionRepository.isOwned(userId, questionId);
    }
}
