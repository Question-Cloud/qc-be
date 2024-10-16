package com.eager.questioncloud.question.model;

import com.eager.questioncloud.question.entity.QuestionEntity;
import com.eager.questioncloud.question.vo.QuestionContent;
import com.eager.questioncloud.question.vo.QuestionStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Question {
    private Long id;
    private Long creatorId;
    private QuestionContent questionContent;
    private QuestionStatus questionStatus;
    private int count;
    private LocalDateTime createdAt;

    @Builder
    public Question(Long id, Long creatorId, QuestionContent questionContent, QuestionStatus questionStatus, int count, LocalDateTime createdAt) {
        this.id = id;
        this.creatorId = creatorId;
        this.questionContent = questionContent;
        this.questionStatus = questionStatus;
        this.count = count;
        this.createdAt = createdAt;
    }

    public static Question create(Long creatorId, QuestionContent questionContent) {
        return Question.builder()
            .creatorId(creatorId)
            .questionContent(questionContent)
            .questionStatus(QuestionStatus.Available)
            .count(0)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public void modify(QuestionContent questionContent) {
        this.questionContent = questionContent;
    }

    public void delete() {
        this.questionStatus = QuestionStatus.Delete;
    }

    public QuestionEntity toEntity() {
        return QuestionEntity.builder()
            .id(id)
            .creatorId(creatorId)
            .questionContent(questionContent)
            .questionStatus(questionStatus)
            .count(count)
            .createdAt(createdAt)
            .build();
    }
}
