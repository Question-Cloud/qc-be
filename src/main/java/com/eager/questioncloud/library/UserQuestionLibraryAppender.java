package com.eager.questioncloud.library;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
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
        return userQuestionLibraryRepository.append(UserQuestionLibrary.create(userId, questionIds));
    }
}
