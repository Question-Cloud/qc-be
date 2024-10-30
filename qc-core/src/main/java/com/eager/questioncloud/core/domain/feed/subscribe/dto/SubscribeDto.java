package com.eager.questioncloud.core.domain.feed.subscribe.dto;

import com.eager.questioncloud.core.domain.creator.dto.CreatorDto.CreatorSimpleInformation;
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
