package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.question.api.internal.ModifyQuestionAPIRequest
import com.eager.questioncloud.question.api.internal.QuestionCommandAPI
import com.eager.questioncloud.workspace.command.ModifyQuestionCommand
import org.springframework.stereotype.Component

@Component
class WorkspaceQuestionUpdater(
    private val questionCommandAPI: QuestionCommandAPI,
) {
    fun modifyQuestion(command: ModifyQuestionCommand) {
        questionCommandAPI.modify(
            ModifyQuestionAPIRequest(
                command.creatorId,
                command.questionId,
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