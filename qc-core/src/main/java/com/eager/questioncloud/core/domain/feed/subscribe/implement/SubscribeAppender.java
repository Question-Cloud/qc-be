package com.eager.questioncloud.core.domain.feed.subscribe.implement;

import com.eager.questioncloud.core.domain.feed.subscribe.model.Subscribe;
import com.eager.questioncloud.core.domain.feed.subscribe.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscribeAppender {
    private final SubscribeRepository subscribeRepository;

    public Subscribe append(Subscribe subscribe) {
        return subscribeRepository.save(subscribe);
    }
}
