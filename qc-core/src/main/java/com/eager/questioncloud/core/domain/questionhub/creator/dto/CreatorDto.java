package com.eager.questioncloud.core.domain.questionhub.creator.dto;

import com.eager.questioncloud.core.domain.questionhub.question.vo.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CreatorDto {
    @Getter
    public static class CreatorInformation {
        private Long creatorId;
        private String name;
        private String profileImage;
        private Subject mainSubject;
        private String email;
        private int salesCount;
        private Double rate;
        private String introduction;

        @Builder
        public CreatorInformation(Long creatorId, String name, String profileImage, Subject mainSubject, String email, int salesCount, Double rate,
            String introduction) {
            this.creatorId = creatorId;
            this.name = name;
            this.profileImage = profileImage;
            this.mainSubject = mainSubject;
            this.email = email;
            this.salesCount = salesCount;
            this.rate = rate;
            this.introduction = introduction;
        }
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
