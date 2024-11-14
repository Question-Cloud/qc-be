package com.eager.questioncloud.core.domain.library.implement;

import com.eager.questioncloud.core.domain.library.model.UserQuestion;
import com.eager.questioncloud.core.domain.library.repository.LibraryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionAppender {
    private final LibraryRepository libraryRepository;

    public List<UserQuestion> appendUserQuestions(Long userId, List<Long> questionIds) {
        return libraryRepository.saveAll(UserQuestion.create(userId, questionIds));
    }
}
