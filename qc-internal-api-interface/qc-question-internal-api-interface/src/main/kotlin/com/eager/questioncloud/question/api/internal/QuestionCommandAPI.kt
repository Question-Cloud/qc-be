package com.eager.questioncloud.question.api.internal

interface QuestionCommandAPI {
    fun register(creatorId: Long, registerQuestionAPIRequest: RegisterQuestionAPIRequest): Long
    
    fun modify(questionId: Long, modifyQuestionCommand: ModifyQuestionCommand)
    
    fun delete(questionId: Long, creatorId: Long)
}