package com.eager.questioncloud.question.api.internal

interface QuestionCommandAPI {
    fun register(registerQuestionAPIRequest: RegisterQuestionAPIRequest): Long
    
    fun modify(modifyQuestionAPIRequest: ModifyQuestionAPIRequest)
    
    fun delete(questionId: Long, creatorId: Long)
}