package com.eager.questioncloud.library;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionLibraryReader {
    private final UserQuestionLibraryRepository userQuestionLibraryRepository;

    public Boolean isOwned(Long userId, Long questionId) {
        return userQuestionLibraryRepository.isOwned(userId, questionId);
    }
}
