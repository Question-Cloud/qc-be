package com.eager.questioncloud.core.domain.feed.library.implement;

import com.eager.questioncloud.core.domain.feed.library.model.UserQuestion;
import com.eager.questioncloud.core.domain.feed.library.repository.UserQuestionLibraryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionAppender {
    private final UserQuestionLibraryRepository userQuestionLibraryRepository;

    public List<UserQuestion> appendUserQuestions(Long userId, List<Long> questionIds) {
        return userQuestionLibraryRepository.saveAll(UserQuestion.create(userId, questionIds));
    }
}
