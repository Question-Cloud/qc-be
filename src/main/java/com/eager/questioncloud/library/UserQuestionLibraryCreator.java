package com.eager.questioncloud.library;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionLibraryCreator {
    private final UserQuestionLibraryRepository userQuestionLibraryRepository;

    public List<UserQuestionLibrary> append(List<UserQuestionLibrary> userQuestionLibraries) {
        return userQuestionLibraryRepository.append(userQuestionLibraries);
    }
}
