package com.eager.questioncloud.core.domain.library.dto;

import com.eager.questioncloud.core.domain.library.model.UserQuestion;
import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformationForLibrary;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserQuestionDto {
    @Getter
    @AllArgsConstructor
    public static class UserQuestionItem {
        private Long id;
        private Boolean isUsed;
        private QuestionInformationForLibrary question;

        public static List<UserQuestionItem> from(List<UserQuestion> userQuestions) {
            return userQuestions.stream()
                .map(userQuestion -> new UserQuestionItem(
                    userQuestion.getId(),
                    userQuestion.getIsUsed(),
                    QuestionInformationForLibrary.forLibraryDetail(userQuestion.getQuestion()))
                )
                .collect(Collectors.toList());
        }
    }
}
