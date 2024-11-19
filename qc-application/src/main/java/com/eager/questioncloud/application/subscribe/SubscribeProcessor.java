package com.eager.questioncloud.application.subscribe;

import com.eager.questioncloud.domain.creator.CreatorRepository;
import com.eager.questioncloud.domain.subscribe.Subscribe;
import com.eager.questioncloud.domain.subscribe.SubscribeRepository;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SubscribeProcessor {
    private final SubscribeRepository subscribeRepository;
    private final CreatorRepository creatorRepository;

    public Subscribe subscribe(Long userId, Long creatorId) {
        if (!isActiveCreator(creatorId)) {
            throw new CustomException(Error.NOT_FOUND);
        }

        if (isAlreadySubscribed(userId, creatorId)) {
            throw new CustomException(Error.ALREADY_SUBSCRIBE_CREATOR);
        }

        return subscribeRepository.save(Subscribe.create(userId, creatorId));
    }

    @Transactional
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
