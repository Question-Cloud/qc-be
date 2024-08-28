package com.eager.questioncloud.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscribeCreator {
    private final SubscribeRepository subscribeRepository;

    public Subscribe append(Subscribe subscribe) {
        return subscribeRepository.append(subscribe);
    }
}
