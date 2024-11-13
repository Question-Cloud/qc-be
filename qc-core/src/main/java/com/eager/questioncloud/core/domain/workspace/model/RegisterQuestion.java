package com.eager.questioncloud.core.domain.workspace.model;

import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.question.vo.QuestionContent;
import lombok.Getter;

@Getter
public class RegisterQuestion {
    private Creator creator;
    private QuestionContent questionContent;
    private Long questionCategoryId;

    public RegisterQuestion(Creator creator, QuestionContent questionContent, Long questionCategoryId) {
        this.creator = creator;
        this.questionContent = questionContent;
        this.questionCategoryId = questionCategoryId;
    }
}
