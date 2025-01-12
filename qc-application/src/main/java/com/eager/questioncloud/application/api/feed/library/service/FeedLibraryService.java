package com.eager.questioncloud.application.api.feed.library.service;

import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.userquestion.dto.UserQuestionDetail;
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedLibraryService {
    private final UserQuestionRepository userQuestionRepository;

    public List<UserQuestionDetail> getUserQuestions(QuestionFilter questionFilter) {
        return userQuestionRepository.getUserQuestions(questionFilter);
    }

    public int countUserQuestions(QuestionFilter questionFilter) {
        return userQuestionRepository.countUserQuestions(questionFilter);
    }
}
