package com.eager.questioncloud.application.feed;

import com.eager.questioncloud.common.PagingInformation;
import com.eager.questioncloud.domain.creator.CreatorInformation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final FeedSubscribeReader feedSubscribeReader;

    public List<CreatorInformation> getMySubscribeCreators(Long userId, PagingInformation pagingInformation) {
        return feedSubscribeReader.getMySubscribeCreators(userId, pagingInformation);
    }

    public int countMySubscribe(Long userId) {
        return feedSubscribeReader.countMySubscribe(userId);
    }
}
