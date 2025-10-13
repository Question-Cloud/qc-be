package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.question.api.internal.QuestionCommandAPI
import com.eager.questioncloud.workspace.command.DeleteQuestionCommand
import org.springframework.stereotype.Component

@Component
class WorkspaceQuestionRemover(
    private val creatorRepository: CreatorRepository,
    private val questionCommandAPI: QuestionCommandAPI,
) {
    fun deleteQuestion(command: DeleteQuestionCommand) {
        val creator = creatorRepository.findByUserId(command.userId) ?: throw CoreException(Error.NOT_FOUND)
        questionCommandAPI.delete(command.questionId, creator.id)
    }
}