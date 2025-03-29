package com.eager.questioncloud.application.api.workspace.service

import com.eager.questioncloud.application.api.workspace.implement.CreatorStatisticsInitializer
import com.eager.questioncloud.application.api.workspace.implement.WorkspaceCreatorRegister
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.question.enums.Subject
import com.eager.questioncloud.core.domain.user.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorkspaceRegisterService(
    private val workspaceCreatorRegister: WorkspaceCreatorRegister,
    private val creatorStatisticsInitializer: CreatorStatisticsInitializer,
) {
    @Transactional
    fun register(user: User, mainSubject: Subject, introduction: String): Creator {
        val creator = workspaceCreatorRegister.register(user, mainSubject, introduction)
        creatorStatisticsInitializer.initializeCreatorStatistics(creator.id)
        return creator
    }
}
