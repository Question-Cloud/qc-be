package com.eager.questioncloud.core.domain.subscribe.implement;

import com.eager.questioncloud.core.domain.creator.implement.CreatorReader;
import com.eager.questioncloud.core.domain.subscribe.model.Subscribe;
import com.eager.questioncloud.core.domain.subscribe.repository.SubscribeRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SubscribeProcessor {
    private final SubscribeRepository subscribeRepository;
    private final CreatorReader creatorReader;

    public Subscribe subscribe(Long userId, Long creatorId) {
        if (!creatorReader.isExistsCreator(creatorId)) {
            throw new CustomException(Error.NOT_FOUND);
        }

        if (subscribeRepository.isSubscribed(userId, creatorId)) {
            throw new CustomException(Error.ALREADY_SUBSCRIBE_CREATOR);
        }

        return subscribeRepository.save(Subscribe.create(userId, creatorId));
    }

    @Transactional
    public void unSubscribe(Long userId, Long creatorId) {
        subscribeRepository.unSubscribe(userId, creatorId);
    }
}
