package com.eager.questioncloud.application.utils.fixture.helper

import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository
import com.eager.questioncloud.core.domain.point.model.UserPoint

class UserPointFixtureHelper {
    companion object {
        fun createUserPoint(uid: Long, point: Int = 0, userPointRepository: UserPointRepository) {
            userPointRepository.save(UserPoint(uid, point))
        }
    }
}