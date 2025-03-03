package com.eager.questioncloud.core.domain.subscribe.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnsubscribedEvent {
    private Long creatorId;

    public static UnsubscribedEvent create(Long creatorId) {
        return new UnsubscribedEvent(creatorId);
    }
}
