package com.eager.questioncloud.core.domain.cart.service;

import com.eager.questioncloud.core.domain.cart.dto.CartItemDetail;
import com.eager.questioncloud.core.domain.cart.implement.CartItemAppender;
import com.eager.questioncloud.core.domain.cart.implement.CartItemReader;
import com.eager.questioncloud.core.domain.cart.implement.CartItemRemover;
import com.eager.questioncloud.core.domain.cart.model.CartItem;
import com.eager.questioncloud.core.domain.question.implement.QuestionReader;
import com.eager.questioncloud.core.domain.question.model.Question;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartService {
    private final CartItemReader cartItemReader;
    private final CartItemAppender cartItemAppender;
    private final CartItemRemover cartItemRemover;
    private final QuestionReader questionReader;

    public List<CartItemDetail> getCartItems(Long userId) {
        return cartItemReader.getCartItems(userId);
    }

    public void appendCartItem(Long userId, Long questionId) {
        Question question = questionReader.getQuestion(questionId);
        CartItem cartItem = CartItem.create(userId, question.getId());
        cartItemAppender.append(cartItem);
    }

    public void removeCartItem(List<Long> ids, Long userId) {
        cartItemRemover.remove(ids, userId);
    }
}
