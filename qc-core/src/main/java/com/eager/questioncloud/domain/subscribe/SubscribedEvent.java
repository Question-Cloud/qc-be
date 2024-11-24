package com.eager.questioncloud.domain.subscribe;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubscribedEvent {
    private Subscribe subscribe;

    public static SubscribedEvent create(Subscribe subscribe) {
        return new SubscribedEvent(subscribe);
    }
}
