package com.eager.questioncloud.domain.question.model;

import com.eager.questioncloud.domain.question.vo.Subject;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionCategory {
    private Long id;
    private Long parentId;
    private Subject subject;
    private String title;
    private Boolean isParent;

    @Builder
    public QuestionCategory(Long id, Long parentId, Subject subject, String title, Boolean isParent) {
        this.id = id;
        this.parentId = parentId;
        this.subject = subject;
        this.title = title;
        this.isParent = isParent;
    }
}
