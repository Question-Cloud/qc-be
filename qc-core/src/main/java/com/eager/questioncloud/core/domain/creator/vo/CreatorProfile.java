package com.eager.questioncloud.core.domain.creator.vo;

import com.eager.questioncloud.core.domain.question.vo.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatorProfile {
    private Subject mainSubject;
    private String introduction;

    public static CreatorProfile create(Subject mainSubject, String introduction) {
        return new CreatorProfile(mainSubject, introduction);
    }
}
