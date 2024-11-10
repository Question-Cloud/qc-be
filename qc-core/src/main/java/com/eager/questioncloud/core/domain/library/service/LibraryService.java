package com.eager.questioncloud.core.domain.library.service;

import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.library.dto.UserQuestionDto.UserQuestionItem;
import com.eager.questioncloud.core.domain.library.implement.UserQuestionReader;
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
