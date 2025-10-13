package com.eager.questioncloud.workspace.command

data class DeleteQuestionCommand(
    val userId: Long,
    val questionId: Long,
)