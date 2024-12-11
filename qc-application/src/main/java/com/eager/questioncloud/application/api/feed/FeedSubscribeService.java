package com.eager.questioncloud.application.api.feed;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.creator.dto.CreatorInformation;
import com.eager.questioncloud.core.domain.subscribe.infrastructure.SubscribeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedSubscribeService {
    private final SubscribeRepository subscribeRepository;

    public List<CreatorInformation> getMySubscribeCreators(Long userId, PagingInformation pagingInformation) {
        return subscribeRepository.getMySubscribeCreators(userId, pagingInformation);
    }

    public int countMySubscribe(Long userId) {
        return subscribeRepository.countMySubscribe(userId);
    }
}
