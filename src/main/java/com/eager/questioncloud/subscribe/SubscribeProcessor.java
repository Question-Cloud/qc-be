package com.eager.questioncloud.subscribe;

import com.eager.questioncloud.creator.CreatorReader;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscribeProcessor {
    private final SubscribeReader subscribeReader;
    private final SubscribeAppender subscribeAppender;
    private final CreatorReader creatorReader;
    private final SubscribeRemover subscribeRemover;

    public Subscribe subscribe(Long userId, Long creatorId) {
        if (!creatorReader.isExistsCreator(creatorId)) {
            throw new CustomException(Error.NOT_FOUND);
        }

        if (subscribeReader.isSubscribed(userId, creatorId)) {
            throw new CustomException(Error.ALREADY_SUBSCRIBE_CREATOR);
        }

        return subscribeAppender.append(Subscribe.create(userId, creatorId));
    }

    public void unSubscribe(Long userId, Long creatorId) {
        subscribeRemover.unSubscribe(userId, creatorId);
    }
}