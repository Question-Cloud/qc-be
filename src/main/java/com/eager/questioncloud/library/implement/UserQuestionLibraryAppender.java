package com.eager.questioncloud.library.implement;

import com.eager.questioncloud.library.model.UserQuestionLibrary;
import com.eager.questioncloud.library.repository.UserQuestionLibraryRepository;
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
