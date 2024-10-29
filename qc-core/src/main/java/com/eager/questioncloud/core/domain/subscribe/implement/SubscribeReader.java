package com.eager.questioncloud.core.domain.subscribe.implement;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.subscribe.dto.SubscribeDto.SubscribeListItem;
import com.eager.questioncloud.core.domain.subscribe.repository.SubscribeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscribeReader {
    private final SubscribeRepository subscribeRepository;

    public Boolean isSubscribed(Long userId, Long creatorId) {
        return subscribeRepository.isSubscribed(userId, creatorId);
    }

    public int countSubscriber(Long creatorId) {
        return subscribeRepository.countSubscriber(creatorId);
    }

    public List<SubscribeListItem> getMySubscribeList(Long userId, PagingInformation pagingInformation) {
        return subscribeRepository.getMySubscribeList(userId, pagingInformation);
    }

    public int countMySubscribe(Long userId) {
        return subscribeRepository.countMySubscribe(userId);
    }
}
