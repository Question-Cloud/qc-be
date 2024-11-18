package com.eager.questioncloud.application.feed;

import com.eager.questioncloud.domain.question.QuestionFilter;
import com.eager.questioncloud.domain.userquestion.UserQuestionDetail;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedLibraryService {
    private final FeedLibraryReader feedLibraryReader;

    public List<UserQuestionDetail> getUserQuestions(QuestionFilter questionFilter) {
        return feedLibraryReader.getUserQuestions(questionFilter);
    }

    public int countUserQuestions(QuestionFilter questionFilter) {
        return feedLibraryReader.countUserQuestions(questionFilter);
    }
}
