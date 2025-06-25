package com.eager.questioncloud.question.api.internal

interface QuestionCommandAPI {
    fun register(creatorId: Long, registerQuestionCommand: RegisterQuestionCommand): Long

    fun modify(questionId: Long, modifyQuestionCommand: ModifyQuestionCommand)

    fun delete(questionId: Long, creatorId: Long)
}