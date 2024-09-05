package com.eager.questioncloud.library;

import com.eager.questioncloud.question.QuestionDto.QuestionInformationForLibrary;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserQuestionLibraryDto {
    @Getter
    @AllArgsConstructor
    public static class UserQuestionLibraryItem {
        private Long id;
        private Boolean isUsed;
        private QuestionInformationForLibrary question;
    }
}
