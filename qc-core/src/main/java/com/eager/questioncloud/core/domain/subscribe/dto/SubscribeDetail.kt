package com.eager.questioncloud.core.domain.subscribe.dto;

import com.eager.questioncloud.core.domain.question.enums.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubscribeDetail {
    private Long creatorId;
    private String creatorName;
    private String creatorProfileImage;
    private Subject mainSubject;
    private String introduction;
    private int subscribeCount;
}
