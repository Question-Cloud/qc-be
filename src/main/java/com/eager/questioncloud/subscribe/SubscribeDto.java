package com.eager.questioncloud.subscribe;

import com.eager.questioncloud.creator.CreatorDto.CreatorSimpleInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class SubscribeDto {
    @Getter
    @AllArgsConstructor
    public static class SubscribeListItem {
        private Long id;
        private CreatorSimpleInformation creatorInformation;
    }
}
