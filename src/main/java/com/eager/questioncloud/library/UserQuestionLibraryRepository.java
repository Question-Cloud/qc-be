package com.eager.questioncloud.library;

import java.util.List;

public interface UserQuestionLibraryRepository {
    List<UserQuestionLibrary> append(List<UserQuestionLibrary> userQuestionLibraries);
}
