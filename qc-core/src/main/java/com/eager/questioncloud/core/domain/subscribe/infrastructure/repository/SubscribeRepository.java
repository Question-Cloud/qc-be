package com.eager.questioncloud.core.domain.subscribe.infrastructure.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.creator.dto.CreatorInformation;
import com.eager.questioncloud.core.domain.subscribe.model.Subscribe;
import java.util.List;

public interface SubscribeRepository {
    Subscribe save(Subscribe subscribe);

    Boolean isSubscribed(Long subscriberId, Long creatorId);

    void unSubscribe(Long subscriberId, Long creatorId);

    int countSubscriber(Long creatorId);

    List<CreatorInformation> getMySubscribeCreators(Long userId, PagingInformation pagingInformation);

    int countMySubscribe(Long userId);
}
