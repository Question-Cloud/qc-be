package com.eager.questioncloud.core.domain.subscribe.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeJpaRepository extends JpaRepository<SubscribeEntity, Long> {
    Boolean existsBySubscriberIdAndCreatorId(Long subscriberId, Long creatorId);

    void deleteBySubscriberIdAndCreatorId(Long subscriberId, Long creatorId);

    int countByCreatorId(Long creatorId);
}
