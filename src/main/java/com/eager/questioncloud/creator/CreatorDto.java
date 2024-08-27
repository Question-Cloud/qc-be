package com.eager.questioncloud.creator;

import com.eager.questioncloud.question.Subject;
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
        //        private int subscriberCount;
        private int salesCount;
        private Double rate;
        private String introduction;
    }
}
