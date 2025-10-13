package com.eager.questioncloud.question.api.internal

interface QuestionCommandAPI {
    fun register(creatorId: Long, registerQuestionAPIRequest: RegisterQuestionAPIRequest): Long
    
    fun modify(questionId: Long, modifyQuestionAPIRequest: ModifyQuestionAPIRequest)
    
    fun delete(questionId: Long, creatorId: Long)
}