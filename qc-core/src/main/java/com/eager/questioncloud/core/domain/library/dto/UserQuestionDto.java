package com.eager.questioncloud.core.domain.library.dto;

import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformationForLibrary;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserQuestionDto {
    @Getter
    @AllArgsConstructor
    public static class UserQuestionItem {
        private Long id;
        private Boolean isUsed;
        private QuestionInformationForLibrary question;
    }
}
