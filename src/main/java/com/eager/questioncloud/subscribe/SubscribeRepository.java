package com.eager.questioncloud.subscribe;

public interface SubscribeRepository {
    Subscribe append(Subscribe subscribe);

    Boolean checkAlreadySubscribe(Long subscriberId, Long creatorId);
}
