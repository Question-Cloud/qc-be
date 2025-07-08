package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.workspace.implement.CreatorStatisticsInitializer
import com.eager.questioncloud.workspace.implement.WorkspaceCreatorRegister
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorkspaceRegisterService(
    private val workspaceCreatorRegister: WorkspaceCreatorRegister,
    private val creatorStatisticsInitializer: CreatorStatisticsInitializer,
) {
    @Transactional
    fun register(userId: Long, mainSubject: String, introduction: String): Creator {
        val creator = workspaceCreatorRegister.register(userId, mainSubject, introduction)
        creatorStatisticsInitializer.initializeCreatorStatistics(creator.id)
        return creator
    }
}
