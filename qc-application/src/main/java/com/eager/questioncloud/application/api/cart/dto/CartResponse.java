package com.eager.questioncloud.application.api.cart.dto;

import com.eager.questioncloud.core.domain.cart.dto.CartItemDetail;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CartResponse {
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetCartResponse {
        private Boolean success;
        private List<CartItemDetail> items;

        public static GetCartResponse create(List<CartItemDetail> items) {
            return new GetCartResponse(true, items);
        }
    }
}
