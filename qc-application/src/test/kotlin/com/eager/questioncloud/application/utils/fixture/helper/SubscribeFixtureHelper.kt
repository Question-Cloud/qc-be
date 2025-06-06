package com.eager.questioncloud.application.utils.fixture.helper

import com.eager.questioncloud.core.domain.subscribe.infrastructure.repository.SubscribeRepository
import com.eager.questioncloud.core.domain.subscribe.model.Subscribe

class SubscribeFixtureHelper {
    companion object {
        fun createSubscribe(
            subscriberId: Long,
            creatorId: Long,
            subscribeRepository: SubscribeRepository
        ): Subscribe {
            return subscribeRepository.save(
                Subscribe.create(subscriberId = subscriberId, creatorId = creatorId)
            )
        }
    }
}
