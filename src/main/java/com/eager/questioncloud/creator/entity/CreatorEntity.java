package com.eager.questioncloud.creator.entity;

import com.eager.questioncloud.creator.domain.Creator;
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
@Table(name = "creator")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    @Enumerated(EnumType.STRING)
    private Subject mainSubject;

    @Column
    private String introduction;

    @Builder
    public CreatorEntity(Long id, Long userId, Subject mainSubject, String introduction) {
        this.id = id;
        this.userId = userId;
        this.mainSubject = mainSubject;
        this.introduction = introduction;
    }

    public Creator toModel() {
        return Creator.builder()
            .id(id)
            .userId(userId)
            .mainSubject(mainSubject)
            .introduction(introduction)
            .build();
    }
}
