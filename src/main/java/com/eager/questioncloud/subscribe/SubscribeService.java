package com.eager.questioncloud.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final SubscribeProcessor subscribeProcessor;

    public Subscribe subscribe(Long userId, Long creatorId) {
        return subscribeProcessor.subscribe(userId, creatorId);
    }
}
