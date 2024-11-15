package com.eager.questioncloud.core.domain.creator.dto;

import com.eager.questioncloud.core.domain.question.vo.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CreatorDto {
    @Getter
    @AllArgsConstructor
    public static class CreatorInformation {
        private Long creatorId;
        private String name;
        private String profileImage;
        private Subject mainSubject;
        private String email;
        private int salesCount;
        private Double rate;
        private String introduction;
    }

    @Getter
    @AllArgsConstructor
    public static class CreatorSimpleInformation {
        private Long creatorId;
        private String name;
        private String profileImage;
        private Subject mainSubject;
        private String introduction;
        private int subscribeCount;
    }
}
