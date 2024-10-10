package com.eager.questioncloud.library.implement;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.library.domain.UserQuestionLibrary;
import com.eager.questioncloud.library.repository.UserQuestionLibraryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionLibraryAppender {
    private final UserQuestionLibraryRepository userQuestionLibraryRepository;

    public List<UserQuestionLibrary> appendUserQuestion(Long userId, List<Long> questionIds) {
        if (userQuestionLibraryRepository.checkDuplicate(userId, questionIds)) {
            throw new CustomException(Error.ALREADY_OWN_QUESTION);
        }
        return userQuestionLibraryRepository.saveAll(UserQuestionLibrary.create(userId, questionIds));
    }
}
