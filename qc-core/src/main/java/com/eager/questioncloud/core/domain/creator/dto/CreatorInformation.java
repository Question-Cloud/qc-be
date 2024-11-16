package com.eager.questioncloud.core.domain.creator.dto;

import com.eager.questioncloud.core.domain.question.vo.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatorInformation {
    private Long creatorId;
    private String name;
    private String profileImage;
    private Subject mainSubject;
    private String email;
    private int salesCount;
    private Double rate;
    private String introduction;
}
