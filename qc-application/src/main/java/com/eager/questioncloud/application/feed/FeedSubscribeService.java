package com.eager.questioncloud.application.feed;

import com.eager.questioncloud.common.PagingInformation;
import com.eager.questioncloud.domain.creator.CreatorInformation;
import com.eager.questioncloud.domain.subscribe.SubscribeRepository;
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
