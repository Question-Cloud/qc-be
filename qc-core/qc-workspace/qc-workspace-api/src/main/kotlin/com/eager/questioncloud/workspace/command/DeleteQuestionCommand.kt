package com.eager.questioncloud.workspace.command

data class DeleteQuestionCommand(
    val creatorId: Long,
    val questionId: Long,
)