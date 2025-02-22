package com.eager.questioncloud.core.domain.subscribe.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Subscribe {
    private Long id;
    private Long subscriberId;
    private Long creatorId;
    private LocalDateTime createdAt;

    @Builder
    public Subscribe(Long id, Long subscriberId, Long creatorId, LocalDateTime createdAt) {
        this.id = id;
        this.subscriberId = subscriberId;
        this.creatorId = creatorId;
        this.createdAt = createdAt;
    }

    public static Subscribe create(Long subscriberId, Long creatorId) {
        return Subscribe.builder()
            .subscriberId(subscriberId)
            .creatorId(creatorId)
            .createdAt(LocalDateTime.now())
            .build();
    }
}
