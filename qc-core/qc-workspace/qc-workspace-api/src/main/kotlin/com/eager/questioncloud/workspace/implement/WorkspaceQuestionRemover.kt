package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.question.api.internal.QuestionCommandAPI
import com.eager.questioncloud.workspace.command.DeleteQuestionCommand
import org.springframework.stereotype.Component

@Component
class WorkspaceQuestionRemover(
    private val questionCommandAPI: QuestionCommandAPI,
) {
    fun deleteQuestion(command: DeleteQuestionCommand) {
        questionCommandAPI.delete(command.questionId, command.creatorId)
    }
}