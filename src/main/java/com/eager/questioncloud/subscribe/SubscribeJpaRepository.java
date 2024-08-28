package com.eager.questioncloud.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeJpaRepository extends JpaRepository<SubscribeEntity, Long> {
    Boolean existsBySubscriberIdAndCreatorId(Long subscriberId, Long creatorId);
}
