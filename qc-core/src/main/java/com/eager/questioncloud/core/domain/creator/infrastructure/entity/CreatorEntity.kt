package com.eager.questioncloud.core.domain.creator.infrastructure.entity;

import com.eager.questioncloud.core.domain.creator.model.Creator;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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

    @Embedded
    private CreatorProfileEntity creatorProfileEntity;

    @Builder
    public CreatorEntity(Long id, Long userId, CreatorProfileEntity creatorProfileEntity) {
        this.id = id;
        this.userId = userId;
        this.creatorProfileEntity = creatorProfileEntity;
    }

    public Creator toModel() {
        return Creator.builder()
            .id(id)
            .userId(userId)
            .creatorProfile(creatorProfileEntity.toModel())
            .build();
    }

    public static CreatorEntity from(Creator creator) {
        return CreatorEntity.builder()
            .id(creator.getId())
            .userId(creator.getUserId())
            .creatorProfileEntity(CreatorProfileEntity.from(creator.getCreatorProfile()))
            .build();
    }
}
