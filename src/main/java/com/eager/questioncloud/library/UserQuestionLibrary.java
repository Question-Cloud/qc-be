package com.eager.questioncloud.library;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserQuestionLibrary {
    private Long id;
    private Long userId;
    private Long questionId;
    private Boolean isUsed;
    private LocalDateTime createdAt;

    @Builder
    public UserQuestionLibrary(Long id, Long userId, Long questionId, Boolean isUsed, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.isUsed = isUsed;
        this.createdAt = createdAt;
    }

    public UserQuestionLibraryEntity toEntity() {
        return UserQuestionLibraryEntity.builder()
            .id(id)
            .userId(userId)
            .questionId(questionId)
            .isUsed(isUsed)
            .createdAt(createdAt)
            .build();
    }
}
