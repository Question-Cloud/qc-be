package com.eager.questioncloud.core.domain.creator.model;

import com.eager.questioncloud.core.domain.creator.vo.CreatorProfile;
import com.eager.questioncloud.core.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Creator {
    private Long id;
    private User user;
    private CreatorProfile creatorProfile;

    @Builder
    public Creator(Long id, User user, CreatorProfile creatorProfile) {
        this.id = id;
        this.user = user;
        this.creatorProfile = creatorProfile;
    }

    public static Creator create(User user, CreatorProfile creatorProfile) {
        return Creator.builder()
            .user(user)
            .creatorProfile(creatorProfile)
            .build();
    }

    public void updateProfile(CreatorProfile creatorProfile) {
        this.creatorProfile = creatorProfile;
    }
}
