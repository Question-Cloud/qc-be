package com.eager.questioncloud.core.domain.creator.infrastructure.entity;

import com.eager.questioncloud.core.domain.creator.model.CreatorProfile;
import com.eager.questioncloud.core.domain.question.enums.Subject;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CreatorProfileEntity {
    @Enumerated(EnumType.STRING)
    private Subject mainSubject;
    private String introduction;

    public static CreatorProfileEntity from(CreatorProfile creatorProfile) {
        return new CreatorProfileEntity(creatorProfile.getMainSubject(), creatorProfile.getIntroduction());
    }

    public CreatorProfile toModel() {
        return CreatorProfile.create(mainSubject, introduction);
    }
}
