package com.eager.questioncloud.core.domain.questionhub.question.model;

import com.eager.questioncloud.core.domain.questionhub.question.vo.QuestionContent;
import com.eager.questioncloud.core.domain.questionhub.question.vo.QuestionStatus;
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
}
