package com.eager.questioncloud.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final SubscribeProcessor subscribeProcessor;
    private final SubscribeReader subscribeReader;

    public Subscribe subscribe(Long userId, Long creatorId) {
        return subscribeProcessor.subscribe(userId, creatorId);
    }

    public void unSubscribe(Long userId, Long creatorId) {
        subscribeProcessor.unSubscribe(userId, creatorId);
    }

    public Boolean isSubscribed(Long userId, Long creatorId) {
        return subscribeReader.isSubscribed(userId, creatorId);
    }
}
