package com.eager.questioncloud.domain.creator.vo;

import com.eager.questioncloud.domain.question.vo.Subject;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CreatorProfile {
    @Enumerated(EnumType.STRING)
    private Subject mainSubject;
    private String introduction;

    public static CreatorProfile create(Subject mainSubject, String introduction) {
        return new CreatorProfile(mainSubject, introduction);
    }
}
