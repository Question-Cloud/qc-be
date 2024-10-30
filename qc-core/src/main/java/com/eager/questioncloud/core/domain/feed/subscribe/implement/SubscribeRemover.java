package com.eager.questioncloud.core.domain.feed.subscribe.implement;

import com.eager.questioncloud.core.domain.feed.subscribe.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SubscribeRemover {
    private final SubscribeRepository subscribeRepository;

    @Transactional
    public void unSubscribe(Long userId, Long creatorId) {
        subscribeRepository.unSubscribe(userId, creatorId);
    }
}
