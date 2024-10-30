package com.eager.questioncloud.core.domain.feed.subscribe.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.feed.subscribe.dto.SubscribeDto.SubscribeListItem;
import com.eager.questioncloud.core.domain.feed.subscribe.model.Subscribe;
import java.util.List;

public interface SubscribeRepository {
    Subscribe save(Subscribe subscribe);

    Boolean isSubscribed(Long subscriberId, Long creatorId);

    void unSubscribe(Long subscriberId, Long creatorId);

    int countSubscriber(Long creatorId);

    List<SubscribeListItem> getMySubscribeList(Long userId, PagingInformation pagingInformation);

    int countMySubscribe(Long userId);
}