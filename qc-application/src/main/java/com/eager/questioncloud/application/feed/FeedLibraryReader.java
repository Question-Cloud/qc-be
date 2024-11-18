package com.eager.questioncloud.application.feed;

import com.eager.questioncloud.domain.question.QuestionFilter;
import com.eager.questioncloud.domain.userquestion.UserQuestionDetail;
import com.eager.questioncloud.domain.userquestion.UserQuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedLibraryReader {
    private final UserQuestionRepository userQuestionRepository;

    public List<UserQuestionDetail> getUserQuestions(QuestionFilter questionFilter) {
        return userQuestionRepository.getUserQuestions(questionFilter);
    }

    public int countUserQuestions(QuestionFilter questionFilter) {
        return userQuestionRepository.countUserQuestions(questionFilter);
    }
}
