package com.eager.questioncloud.api.cart;

import java.util.List;
import lombok.Getter;

public class Request {
    @Getter
    public static class AddCartItemRequest {
        private Long questionId;
    }

    @Getter
    public static class RemoveCartItemRequest {
        private List<Long> ids;
    }
}
