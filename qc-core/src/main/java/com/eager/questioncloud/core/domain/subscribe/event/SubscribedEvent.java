package com.eager.questioncloud.core.domain.subscribe.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubscribedEvent {
    private Long creatorId;

    public static SubscribedEvent create(Long creatorId) {
        return new SubscribedEvent(creatorId);
    }
}
