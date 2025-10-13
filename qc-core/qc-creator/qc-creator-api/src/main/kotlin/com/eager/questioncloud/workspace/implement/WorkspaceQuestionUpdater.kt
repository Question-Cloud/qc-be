package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.question.api.internal.ModifyQuestionAPIRequest
import com.eager.questioncloud.question.api.internal.QuestionCommandAPI
import com.eager.questioncloud.workspace.command.ModifyQuestionCommand
import org.springframework.stereotype.Component

@Component
class WorkspaceQuestionUpdater(
    private val creatorRepository: CreatorRepository,
    private val questionCommandAPI: QuestionCommandAPI,
) {
    fun modifyQuestion(command: ModifyQuestionCommand) {
        val creator = creatorRepository.findByUserId(command.userId) ?: throw CoreException(Error.NOT_FOUND)
        questionCommandAPI.modify(
            creator.id,
            ModifyQuestionAPIRequest(
                command.questionCategoryId,
                command.subject,
                command.title,
                command.description,
                command.thumbnail,
                command.fileUrl,
                command.explanationUrl,
                command.questionLevel,
                command.price
            )
        )
    }
}