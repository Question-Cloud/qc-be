package com.eager.questioncloud.api.cart;

import com.eager.questioncloud.core.domain.cart.dto.CartItemInformation;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetCartResponse {
        private Boolean success;
        private List<CartItemInformation> items;

        public static GetCartResponse create(List<CartItemInformation> items) {
            return new GetCartResponse(true, items);
        }
    }
}
