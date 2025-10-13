package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.question.api.internal.QuestionCommandAPI
import com.eager.questioncloud.question.api.internal.RegisterQuestionAPIRequest
import com.eager.questioncloud.workspace.command.RegisterQuestionCommand
import org.springframework.stereotype.Component

@Component
class WorkspaceQuestionRegister(
    private val creatorRepository: CreatorRepository,
    private val questionCommandAPI: QuestionCommandAPI,
) {
    fun registerQuestion(command: RegisterQuestionCommand) {
        val creator = creatorRepository.findByUserId(command.userId) ?: throw CoreException(Error.NOT_FOUND)
        questionCommandAPI.register(
            creator.id,
            RegisterQuestionAPIRequest(
                command.questionCategoryId,
                command.subject,
                command.title,
                command.description,
                command.thumbnail,
                command.fileUrl,
                command.explanationUrl,
                command.questionLevel,
                command.price,
            )
        )
    }
}