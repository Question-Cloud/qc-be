package com.eager.questioncloud.library;

import com.eager.questioncloud.question.UserQuestionLibraryReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQuestionLibraryService {
    private final UserQuestionLibraryReader userQuestionLibraryReader;

    public Boolean isOwned(Long userId, Long questionId) {
        return userQuestionLibraryReader.isOwned(userId, questionId);
    }
}
