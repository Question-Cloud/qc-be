package com.eager.questioncloud.domain.subscribe;

import com.eager.questioncloud.common.PagingInformation;
import com.eager.questioncloud.domain.creator.CreatorInformation;
import java.util.List;

public interface SubscribeRepository {
    Subscribe save(Subscribe subscribe);

    Boolean isSubscribed(Long subscriberId, Long creatorId);

    void unSubscribe(Long subscriberId, Long creatorId);

    int countSubscriber(Long creatorId);

    List<CreatorInformation> getMySubscribeCreators(Long userId, PagingInformation pagingInformation);

    int countMySubscribe(Long userId);
}
