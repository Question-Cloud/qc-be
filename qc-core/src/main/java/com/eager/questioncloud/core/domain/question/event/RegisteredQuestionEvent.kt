package com.eager.questioncloud.core.domain.question.event

import com.eager.questioncloud.core.domain.question.model.Question

class RegisteredQuestionEvent(
    val question: Question
) {
    companion object {
        @JvmStatic
        fun create(question: Question): RegisteredQuestionEvent {
            return RegisteredQuestionEvent(question)
        }
    }
}
