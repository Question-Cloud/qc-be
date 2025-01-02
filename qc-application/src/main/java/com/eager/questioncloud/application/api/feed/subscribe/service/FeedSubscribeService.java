package com.eager.questioncloud.application.api.feed.subscribe.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.subscribe.dto.SubscribeDetail;
import com.eager.questioncloud.core.domain.subscribe.infrastructure.repository.SubscribeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedSubscribeService {
    private final SubscribeRepository subscribeRepository;

    public List<SubscribeDetail> getMySubscribes(Long userId, PagingInformation pagingInformation) {
        return subscribeRepository.getMySubscribes(userId, pagingInformation);
    }

    public int countMySubscribe(Long userId) {
        return subscribeRepository.countMySubscribe(userId);
    }
}
