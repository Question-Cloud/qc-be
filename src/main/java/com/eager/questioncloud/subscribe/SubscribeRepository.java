package com.eager.questioncloud.subscribe;

public interface SubscribeRepository {
    Subscribe append(Subscribe subscribe);

    Boolean isSubscribed(Long subscriberId, Long creatorId);

    void unSubscribe(Long subscriberId, Long creatorId);
}
