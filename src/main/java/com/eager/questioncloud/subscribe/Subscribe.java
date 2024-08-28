package com.eager.questioncloud.subscribe;

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

    public SubscribeEntity toEntity() {
        return SubscribeEntity.builder()
            .id(id)
            .subscriberId(subscriberId)
            .creatorId(creatorId)
            .createdAt(createdAt)
            .build();
    }
}
