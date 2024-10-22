package com.eager.questioncloud.domain.subscribe.implement;

import com.eager.questioncloud.domain.subscribe.dto.SubscribeDto.SubscribeListItem;
import com.eager.questioncloud.domain.subscribe.repository.SubscribeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    public List<SubscribeListItem> getMySubscribeList(Long userId, Pageable pageable) {
        return subscribeRepository.getMySubscribeList(userId, pageable);
    }

    public int countMySubscribe(Long userId) {
        return subscribeRepository.countMySubscribe(userId);
    }
}
