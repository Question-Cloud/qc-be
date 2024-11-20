package com.eager.questioncloud.domain.cart;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CartItem {
    private Long id;
    private Long userId;
    private Long questionId;

    @Builder
    public CartItem(Long id, Long userId, Long questionId) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
    }

    public static CartItem create(Long userId, Long questionId) {
        return CartItem.builder()
            .userId(userId)
            .questionId(questionId)
            .build();
    }
}

