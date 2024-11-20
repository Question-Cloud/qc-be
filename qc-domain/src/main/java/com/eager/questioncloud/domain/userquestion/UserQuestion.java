package com.eager.questioncloud.domain.userquestion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserQuestion {
    private Long id;
    private Long userId;
    private Long questionId;
    private Boolean isUsed;
    private LocalDateTime createdAt;

    @Builder
    public UserQuestion(Long id, Long userId, Long questionId, Boolean isUsed, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.isUsed = isUsed;
        this.createdAt = createdAt;
    }

    public static List<UserQuestion> create(Long userId, List<Long> questionIds) {
        LocalDateTime now = LocalDateTime.now();
        return questionIds
            .stream()
            .map(questionId -> UserQuestion
                .builder()
                .userId(userId)
                .questionId(questionId)
                .isUsed(false)
                .createdAt(now)
                .build())
            .collect(Collectors.toList());
    }
}
