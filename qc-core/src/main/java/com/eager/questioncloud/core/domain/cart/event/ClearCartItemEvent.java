package com.eager.questioncloud.core.domain.cart.event;

import java.util.List;
import lombok.Getter;

@Getter
public class ClearCartItemEvent {
    private Long userId;
    private List<Long> questionIds;

    private ClearCartItemEvent(Long userId, List<Long> questionIds) {
        this.userId = userId;
        this.questionIds = questionIds;
    }

    public static ClearCartItemEvent create(Long userId, List<Long> questionIds) {
        return new ClearCartItemEvent(userId, questionIds);
    }
}
