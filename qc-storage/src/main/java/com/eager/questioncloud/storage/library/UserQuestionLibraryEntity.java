package com.eager.questioncloud.storage.library;

import com.eager.questioncloud.core.domain.feed.library.model.UserQuestion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_question_library")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserQuestionLibraryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private Long questionId;

    @Column
    private Boolean isUsed;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public UserQuestionLibraryEntity(Long id, Long userId, Long questionId, Boolean isUsed, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.isUsed = isUsed;
        this.createdAt = createdAt;
    }

    public static List<UserQuestion> toModel(List<UserQuestionLibraryEntity> userQuestionLibraryEntities) {
        return userQuestionLibraryEntities
            .stream()
            .map(UserQuestionLibraryEntity::toModel)
            .collect(Collectors.toList());
    }

    public UserQuestion toModel() {
        return UserQuestion.builder()
            .id(id)
            .userId(userId)
            .questionId(questionId)
            .isUsed(isUsed)
            .createdAt(createdAt)
            .build();
    }

    public static List<UserQuestionLibraryEntity> from(List<UserQuestion> userQuestionLibraries) {
        return userQuestionLibraries.stream()
            .map(UserQuestionLibraryEntity::from)
            .collect(Collectors.toList());
    }

    public static UserQuestionLibraryEntity from(UserQuestion userQuestion) {
        return UserQuestionLibraryEntity.builder()
            .id(userQuestion.getId())
            .userId(userQuestion.getUserId())
            .questionId(userQuestion.getQuestionId())
            .isUsed(userQuestion.getIsUsed())
            .createdAt(userQuestion.getCreatedAt())
            .build();
    }
}
