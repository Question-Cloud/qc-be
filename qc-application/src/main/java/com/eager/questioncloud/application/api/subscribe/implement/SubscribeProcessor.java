package com.eager.questioncloud.application.api.subscribe.implement;

import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository;
import com.eager.questioncloud.core.domain.subscribe.infrastructure.SubscribeRepository;
import com.eager.questioncloud.core.domain.subscribe.model.Subscribe;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscribeProcessor {
    private final SubscribeRepository subscribeRepository;
    private final CreatorRepository creatorRepository;

    public Subscribe subscribe(Long userId, Long creatorId) {
        if (!isActiveCreator(creatorId)) {
            throw new CoreException(Error.NOT_FOUND);
        }

        if (isAlreadySubscribed(userId, creatorId)) {
            throw new CoreException(Error.ALREADY_SUBSCRIBE_CREATOR);
        }

        return subscribeRepository.save(Subscribe.create(userId, creatorId));
    }

    public void unSubscribe(Long userId, Long creatorId) {
        subscribeRepository.unSubscribe(userId, creatorId);
    }

    private Boolean isActiveCreator(Long creatorId) {
        return creatorRepository.existsById(creatorId);
    }

    private Boolean isAlreadySubscribed(Long userId, Long creatorId) {
        return subscribeRepository.isSubscribed(userId, creatorId);
    }
}
