package com.eager.questioncloud.core.domain.library.service;

import com.eager.questioncloud.core.domain.library.dto.UserQuestionDetail;
import com.eager.questioncloud.core.domain.library.implement.UserQuestionReader;
import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final UserQuestionReader userQuestionReader;

    public List<UserQuestionDetail> getUserQuestions(QuestionFilter questionFilter) {
        return userQuestionReader.getUserQuestions(questionFilter);
    }

    public int countUserQuestions(QuestionFilter questionFilter) {
        return userQuestionReader.countUserQuestions(questionFilter);
    }
}
