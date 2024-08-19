package com.eager.questioncloud.library;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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

    public static List<UserQuestionLibrary> create(Long userId, List<Long> questionIds) {
        LocalDateTime now = LocalDateTime.now();
        return questionIds
            .stream()
            .map(questionId -> UserQuestionLibrary
                .builder()
                .userId(userId)
                .questionId(questionId)
                .isUsed(false)
                .createdAt(now)
                .build())
            .collect(Collectors.toList());
    }

    public static List<UserQuestionLibraryEntity> toEntity(List<UserQuestionLibrary> userQuestionLibraries) {
        return userQuestionLibraries
            .stream()
            .map(UserQuestionLibrary::toEntity)
            .collect(Collectors.toList());
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
