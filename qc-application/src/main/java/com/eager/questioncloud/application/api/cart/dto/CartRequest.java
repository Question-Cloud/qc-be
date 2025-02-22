package com.eager.questioncloud.application.api.cart.dto;

import java.util.List;
import lombok.Getter;

public class CartRequest {
    @Getter
    public static class AddCartItemRequest {
        private Long questionId;
    }

    @Getter
    public static class RemoveCartItemRequest {
        private List<Long> ids;
    }
}
