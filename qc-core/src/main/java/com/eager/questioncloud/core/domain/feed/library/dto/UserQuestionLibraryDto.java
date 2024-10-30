package com.eager.questioncloud.core.domain.feed.library.dto;

import com.eager.questioncloud.core.domain.hub.question.dto.QuestionDto.QuestionInformationForLibrary;
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
