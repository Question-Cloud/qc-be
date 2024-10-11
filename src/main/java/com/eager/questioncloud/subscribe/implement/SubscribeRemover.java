package com.eager.questioncloud.subscribe.implement;

import com.eager.questioncloud.subscribe.repository.SubscribeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscribeRemover {
    private final SubscribeRepository subscribeRepository;

    @Transactional
    public void unSubscribe(Long userId, Long creatorId) {
        subscribeRepository.unSubscribe(userId, creatorId);
    }
}
