package com.eager.questioncloud.question.domain

import com.eager.questioncloud.question.enums.QuestionStatus
import java.time.LocalDateTime

class Question(
    var id: Long = 0,
    var creatorId: Long,
    var questionContent: QuestionContent,
    var questionStatus: QuestionStatus = QuestionStatus.Available,
    var count: Int = 0,
    var createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun modify(questionContent: QuestionContent) {
        this.questionContent = questionContent
    }

    fun delete() {
        this.questionStatus = QuestionStatus.Delete
    }

    companion object {
        fun create(
            creatorId: Long,
            questionContent: QuestionContent
        ): Question {
            return Question(
                creatorId = creatorId,
                questionContent = questionContent
            )
        }
    }
}
