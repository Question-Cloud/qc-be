package com.eager.questioncloud.core.domain.subscribe.event;

import com.eager.questioncloud.core.domain.subscribe.model.Subscribe;
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
