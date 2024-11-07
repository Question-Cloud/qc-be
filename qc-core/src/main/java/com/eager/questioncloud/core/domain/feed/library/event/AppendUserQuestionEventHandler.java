package com.eager.questioncloud.core.domain.feed.library.event;

import com.eager.questioncloud.core.domain.feed.library.implement.UserQuestionAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppendUserQuestionEventHandler {
    private final UserQuestionAppender userQuestionAppender;

    @EventListener
    public void appendUserQuestion(AppendUserQuestionEvent appendUserQuestionEvent) {
        userQuestionAppender.appendUserQuestions(appendUserQuestionEvent.getUserId(), appendUserQuestionEvent.getQuestionIds());
    }
}
