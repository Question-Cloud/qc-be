package com.eager.questioncloud.core.domain.cart.event;

import java.util.List;
import lombok.Getter;

@Getter
public class ClearCartItemEvent {
    private Long userId;
    private List<Long> questionIds;
}
