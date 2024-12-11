package com.eager.questioncloud.core.domain.creator;

import com.eager.questioncloud.core.domain.question.Subject;
import lombok.Builder;

@Builder
public class CreatorBuilder {
    private Long id;
    @Builder.Default
    private Long userId = 1L;
    @Builder.Default
    private CreatorProfile creatorProfile = CreatorProfile.create(Subject.Biology, "creatorIntroduction");

    public Creator toCreator() {
        return Creator.builder()
            .id(id)
            .userId(userId)
            .creatorProfile(creatorProfile)
            .build();
    }
}
