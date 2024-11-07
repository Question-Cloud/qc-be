package com.eager.questioncloud.core.domain.feed.library.event;

import java.util.List;
import lombok.Getter;

@Getter
public class AppendUserQuestionEvent {
    private Long userId;
    private List<Long> questionIds;

    private AppendUserQuestionEvent(Long userId, List<Long> questionIds) {
        this.userId = userId;
        this.questionIds = questionIds;
    }

    public static AppendUserQuestionEvent create(Long userId, List<Long> questionIds) {
        return new AppendUserQuestionEvent(userId, questionIds);
    }
}
