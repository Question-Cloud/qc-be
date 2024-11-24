package com.eager.questioncloud.core.domain.creator;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Creator {
    private Long id;
    private Long userId;
    private CreatorProfile creatorProfile;

    @Builder
    public Creator(Long id, Long userId, CreatorProfile creatorProfile) {
        this.id = id;
        this.userId = userId;
        this.creatorProfile = creatorProfile;
    }

    public static Creator create(Long userId, CreatorProfile creatorProfile) {
        return Creator.builder()
            .userId(userId)
            .creatorProfile(creatorProfile)
            .build();
    }

    public void updateProfile(CreatorProfile creatorProfile) {
        this.creatorProfile = creatorProfile;
    }
}
