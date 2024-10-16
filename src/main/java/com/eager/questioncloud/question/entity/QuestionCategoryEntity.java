package com.eager.questioncloud.question.entity;

import com.eager.questioncloud.question.model.QuestionCategory;
import com.eager.questioncloud.question.vo.Subject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "question_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long parentId;

    @Column
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @Column
    private String title;

    @Column
    private Boolean isParent;

    @Builder
    public QuestionCategoryEntity(Long id, Long parentId, Subject subject, String title, Boolean isParent) {
        this.id = id;
        this.parentId = parentId;
        this.subject = subject;
        this.title = title;
        this.isParent = isParent;
    }

    public QuestionCategory toDomain() {
        return QuestionCategory.builder()
            .id(id)
            .parentId(parentId)
            .subject(subject)
            .title(title)
            .isParent(isParent)
            .build();
    }
}
