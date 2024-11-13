package com.eager.questioncloud.core.domain.workspace.model;

import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.question.vo.QuestionContent;
import lombok.Getter;

@Getter
public class ModifyQuestion {
    private Long questionId;
    private Creator creator;
    private QuestionContent questionContent;
    private Long questionCategoryId;

    public ModifyQuestion(Long questionId, Creator creator, QuestionContent questionContent, Long questionCategoryId) {
        this.questionId = questionId;
        this.creator = creator;
        this.questionContent = questionContent;
        this.questionCategoryId = questionCategoryId;
    }
}
