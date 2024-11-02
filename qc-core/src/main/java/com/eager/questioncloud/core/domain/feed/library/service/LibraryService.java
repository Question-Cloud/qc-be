package com.eager.questioncloud.core.domain.feed.library.service;

import com.eager.questioncloud.core.domain.feed.library.dto.UserQuestionDto.UserQuestionItem;
import com.eager.questioncloud.core.domain.feed.library.implement.UserQuestionReader;
import com.eager.questioncloud.core.domain.hub.question.common.QuestionFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final UserQuestionReader userQuestionReader;

    public List<UserQuestionItem> getUserQuestions(QuestionFilter questionFilter) {
        return userQuestionReader.getUserQuestions(questionFilter);
    }

    public int countUserQuestions(QuestionFilter questionFilter) {
        return userQuestionReader.countUserQuestions(questionFilter);
    }
}
