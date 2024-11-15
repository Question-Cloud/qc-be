package com.eager.questioncloud.core.domain.subscribe.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.creator.dto.CreatorDto.CreatorInformation;
import com.eager.questioncloud.core.domain.subscribe.implement.SubscribeProcessor;
import com.eager.questioncloud.core.domain.subscribe.implement.SubscribeReader;
import com.eager.questioncloud.core.domain.subscribe.model.Subscribe;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final SubscribeProcessor subscribeProcessor;
    private final SubscribeReader subscribeReader;

    public Subscribe subscribe(Long userId, Long creatorId) {
        return subscribeProcessor.subscribe(userId, creatorId);
    }

    public void unSubscribe(Long userId, Long creatorId) {
        subscribeProcessor.unSubscribe(userId, creatorId);
    }

    public Boolean isSubscribed(Long userId, Long creatorId) {
        return subscribeReader.isSubscribed(userId, creatorId);
    }

    public int countSubscriber(Long creatorId) {
        return subscribeReader.countSubscriber(creatorId);
    }

    public List<CreatorInformation> getMySubscribeCreators(Long userId, PagingInformation pagingInformation) {
        return subscribeReader.getMySubscribeCreators(userId, pagingInformation);
    }

    public int countMySubscribe(Long userId) {
        return subscribeReader.countMySubscribe(userId);
    }
}
