package com.eager.questioncloud.domain.library.implement;

import com.eager.questioncloud.domain.library.model.UserQuestionLibrary;
import com.eager.questioncloud.domain.library.repository.UserQuestionLibraryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionLibraryAppender {
    private final UserQuestionLibraryRepository userQuestionLibraryRepository;

    public List<UserQuestionLibrary> appendUserQuestions(Long userId, List<Long> questionIds) {
        return userQuestionLibraryRepository.saveAll(UserQuestionLibrary.create(userId, questionIds));
    }
}
