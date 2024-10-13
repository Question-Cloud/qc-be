package com.eager.questioncloud.subscribe.implement;

import com.eager.questioncloud.subscribe.model.Subscribe;
import com.eager.questioncloud.subscribe.repository.SubscribeRepository;
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
