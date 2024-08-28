package com.eager.questioncloud.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscribeReader {
    private final SubscribeRepository subscribeRepository;

    public Boolean isSubscribed(Long userId, Long creatorId) {
        return subscribeRepository.isSubscribed(userId, creatorId);
    }
}
