package com.eager.questioncloud.application.subscribe;

import com.eager.questioncloud.domain.subscribe.Subscribe;
import com.eager.questioncloud.domain.subscribe.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final SubscribeProcessor subscribeProcessor;
    private final SubscribeRepository subscribeRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    //TODO Event 처리
    public void subscribe(Long userId, Long creatorId) {
        Subscribe subscribe = subscribeProcessor.subscribe(userId, creatorId);
//        applicationEventPublisher.publishEvent(SubscribedEvent.create(subscribe));
    }

    //TODO Event 처리
    public void unSubscribe(Long userId, Long creatorId) {
        subscribeProcessor.unSubscribe(userId, creatorId);
//        applicationEventPublisher.publishEvent(UnsubscribedEvent.create(creatorId));
    }

    public Boolean isSubscribed(Long userId, Long creatorId) {
        return subscribeRepository.isSubscribed(userId, creatorId);
    }

    public int countSubscriber(Long creatorId) {
        return subscribeRepository.countSubscriber(creatorId);
    }
}
