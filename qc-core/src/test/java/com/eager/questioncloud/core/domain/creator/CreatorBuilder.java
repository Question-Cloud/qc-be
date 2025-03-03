package com.eager.questioncloud.core.domain.creator;

import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.model.CreatorProfile;
import com.eager.questioncloud.core.domain.question.enums.Subject;
import lombok.Builder;

@Builder
public class CreatorBuilder {
    private Long id;
    @Builder.Default
    private Long userId = 1L;
    @Builder.Default
    private CreatorProfile creatorProfile = CreatorProfile.create(Subject.Biology, "creatorIntroduction");

    public Creator toCreator() {
        return new Creator(id, userId, creatorProfile);
    }
}
