package com.eager.questioncloud.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubscribeRepositoryImpl implements SubscribeRepository {
    private final SubscribeJpaRepository subscribeJpaRepository;

    @Override
    public Subscribe append(Subscribe subscribe) {
        return subscribeJpaRepository.save(subscribe.toEntity()).toModel();
    }

    @Override
    public Boolean isSubscribed(Long subscriberId, Long creatorId) {
        return subscribeJpaRepository.existsBySubscriberIdAndCreatorId(subscriberId, creatorId);
    }

    @Override
    public void unSubscribe(Long subscriberId, Long creatorId) {
        subscribeJpaRepository.deleteBySubscriberIdAndCreatorId(subscriberId, creatorId);
    }
}
