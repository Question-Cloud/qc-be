package com.eager.questioncloud.domain.userquestion;

import com.eager.questioncloud.domain.question.QuestionLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserQuestionDetail {
    private Long questionId;
    private String title;
    private String parentCategory;
    private String childCategory;
    private String thumbnail;
    private String creatorName;
    private QuestionLevel questionLevel;
    private String fileUrl;
    private String explanationUrl;
}
