package com.eager.questioncloud.core.domain.question.model;

import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.question.vo.QuestionContent;
import com.eager.questioncloud.core.domain.question.vo.QuestionStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Question {
    private Long id;
    private Creator creator;
    private QuestionCategoryInformation category;
    private QuestionContent questionContent;
    private QuestionStatus questionStatus;
    private int count;
    private LocalDateTime createdAt;

    @Builder
    public Question(Long id, Creator creator, QuestionCategoryInformation category, QuestionContent questionContent, QuestionStatus questionStatus,
        int count, LocalDateTime createdAt) {
        this.id = id;
        this.creator = creator;
        this.category = category;
        this.questionContent = questionContent;
        this.questionStatus = questionStatus;
        this.count = count;
        this.createdAt = createdAt;
    }

    public static Question create(Creator creator, QuestionContent questionContent, QuestionCategoryInformation category) {
        return Question.builder()
            .creator(creator)
            .category(category)
            .questionContent(questionContent)
            .questionStatus(QuestionStatus.Available)
            .count(0)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public void modify(QuestionContent questionContent, QuestionCategoryInformation category) {
        this.questionContent = questionContent;
        this.category = category;
    }

    public void delete() {
        this.questionStatus = QuestionStatus.Delete;
    }
}
