package com.eager.questioncloud.subscribe;

import com.eager.questioncloud.creator.CreatorRepository;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscribeProcessor {
    private final SubscribeRepository subscribeRepository;
    private final CreatorRepository creatorRepository;

    public Subscribe subscribe(Long userId, Long creatorId) {
        if (!creatorRepository.existsById(creatorId)) {
            throw new CustomException(Error.NOT_FOUND);
        }

        if (subscribeRepository.isSubscribed(userId, creatorId)) {
            throw new CustomException(Error.ALREADY_SUBSCRIBE_CREATOR);
        }

        return subscribeRepository.append(Subscribe.create(userId, creatorId));
    }

    @Transactional
    public void unSubscribe(Long userId, Long creatorId) {
        subscribeRepository.unSubscribe(userId, creatorId);
    }
}
