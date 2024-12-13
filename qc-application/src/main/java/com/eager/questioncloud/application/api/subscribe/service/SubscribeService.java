package com.eager.questioncloud.application.api.subscribe.service;

import com.eager.questioncloud.application.api.subscribe.implement.SubscribeProcessor;
import com.eager.questioncloud.core.domain.subscribe.event.SubscribedEvent;
import com.eager.questioncloud.core.domain.subscribe.event.UnsubscribedEvent;
import com.eager.questioncloud.core.domain.subscribe.infrastructure.SubscribeRepository;
import com.eager.questioncloud.core.domain.subscribe.model.Subscribe;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final SubscribeProcessor subscribeProcessor;
    private final SubscribeRepository subscribeRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void subscribe(Long userId, Long creatorId) {
        Subscribe subscribe = subscribeProcessor.subscribe(userId, creatorId);
        applicationEventPublisher.publishEvent(SubscribedEvent.create(subscribe));
    }

    public void unSubscribe(Long userId, Long creatorId) {
        subscribeProcessor.unSubscribe(userId, creatorId);
        applicationEventPublisher.publishEvent(UnsubscribedEvent.create(creatorId));
    }

    public Boolean isSubscribed(Long userId, Long creatorId) {
        return subscribeRepository.isSubscribed(userId, creatorId);
    }

    public int countSubscriber(Long creatorId) {
        return subscribeRepository.countSubscriber(creatorId);
    }
}