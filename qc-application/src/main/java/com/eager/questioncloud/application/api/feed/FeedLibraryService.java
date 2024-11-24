package com.eager.questioncloud.application.api.feed;

import com.eager.questioncloud.core.domain.question.QuestionFilter;
import com.eager.questioncloud.core.domain.userquestion.UserQuestionDetail;
import com.eager.questioncloud.core.domain.userquestion.UserQuestionRepository;
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
