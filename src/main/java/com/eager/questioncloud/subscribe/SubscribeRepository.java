package com.eager.questioncloud.subscribe;

import com.eager.questioncloud.subscribe.SubscribeDto.SubscribeListItem;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface SubscribeRepository {
    Subscribe save(Subscribe subscribe);

    Boolean isSubscribed(Long subscriberId, Long creatorId);

    void unSubscribe(Long subscriberId, Long creatorId);

    int countSubscriber(Long creatorId);

    List<SubscribeListItem> getMySubscribeList(Long userId, Pageable pageable);

    int countMySubscribe(Long userId);
}
