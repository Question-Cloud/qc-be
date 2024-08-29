package com.eager.questioncloud.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscribeAppender {
    private final SubscribeRepository subscribeRepository;

    public Subscribe append(Subscribe subscribe) {
        return subscribeRepository.append(subscribe);
    }
}
