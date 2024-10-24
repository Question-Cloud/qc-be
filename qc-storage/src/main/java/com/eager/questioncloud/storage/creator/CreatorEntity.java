package com.eager.questioncloud.storage.creator;

import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.vo.CreatorProfile;
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
    private CreatorProfile creatorProfile;

    @Builder
    public CreatorEntity(Long id, Long userId, CreatorProfile creatorProfile) {
        this.id = id;
        this.userId = userId;
        this.creatorProfile = creatorProfile;
    }

    public Creator toModel() {
        return Creator.builder()
            .id(id)
            .userId(userId)
            .creatorProfile(creatorProfile)
            .build();
    }

    public static CreatorEntity from(Creator creator) {
        return CreatorEntity.builder()
            .id(creator.getId())
            .userId(creator.getUserId())
            .creatorProfile(creator.getCreatorProfile())
            .build();
    }
}
