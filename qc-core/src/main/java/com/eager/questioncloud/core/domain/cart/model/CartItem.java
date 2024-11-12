package com.eager.questioncloud.core.domain.cart.model;

import com.eager.questioncloud.core.domain.cart.vo.CartItemInformation;
import com.eager.questioncloud.core.domain.question.model.Question;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CartItem {
    private Long id;
    private Long userId;
    private CartItemInformation itemInformation;

    @Builder
    public CartItem(Long id, Long userId, CartItemInformation itemInformation) {
        this.id = id;
        this.userId = userId;
        this.itemInformation = itemInformation;
    }

    public static CartItem create(Long userId, Question question) {
        return CartItem.builder()
            .userId(userId)
            .itemInformation(CartItemInformation.create(question))
            .build();
    }
}

