package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.question.api.internal.QuestionCommandAPI
import com.eager.questioncloud.question.api.internal.RegisterQuestionAPIRequest
import com.eager.questioncloud.workspace.command.RegisterQuestionCommand
import org.springframework.stereotype.Component

@Component
class WorkspaceQuestionRegister(
    private val questionCommandAPI: QuestionCommandAPI,
) {
    fun registerQuestion(command: RegisterQuestionCommand) {
        questionCommandAPI.register(
            RegisterQuestionAPIRequest(
                command.creatorId,
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