package com.eager.questioncloud.core.domain.subscribe.infrastructure;

import com.eager.questioncloud.core.domain.subscribe.model.Subscribe;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "subscribe")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscribeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long subscriberId;

    @Column
    private Long creatorId;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public SubscribeEntity(Long id, Long subscriberId, Long creatorId, LocalDateTime createdAt) {
        this.id = id;
        this.subscriberId = subscriberId;
        this.creatorId = creatorId;
        this.createdAt = createdAt;
    }

    public Subscribe toModel() {
        return Subscribe.builder()
            .id(id)
            .subscriberId(subscriberId)
            .creatorId(creatorId)
            .createdAt(createdAt)
            .build();
    }

    public static SubscribeEntity from(Subscribe subscribe) {
        return SubscribeEntity.builder()
            .id(subscribe.getId())
            .subscriberId(subscribe.getSubscriberId())
            .creatorId(subscribe.getCreatorId())
            .createdAt(subscribe.getCreatedAt())
            .build();
    }
}
