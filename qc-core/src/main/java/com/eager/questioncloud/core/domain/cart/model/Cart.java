package com.eager.questioncloud.core.domain.cart.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Cart {
    private Long userId;
    private List<CartItem> items;

    @Builder
    private Cart(Long userId, List<CartItem> items) {
        this.userId = userId;
        this.items = items;
    }

    public static Cart create(Long userId, List<CartItem> items) {
        return Cart.builder()
            .userId(userId)
            .items(items)
            .build();
    }
}
