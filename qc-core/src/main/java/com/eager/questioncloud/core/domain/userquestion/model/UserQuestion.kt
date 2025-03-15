package com.eager.questioncloud.core.domain.userquestion.model

import java.time.LocalDateTime
import java.util.stream.Collectors

class UserQuestion(
    val id: Long? = null,
    val userId: Long,
    val questionId: Long,
    val isUsed: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun create(userId: Long, questionIds: List<Long>): List<UserQuestion> {
            return questionIds
                .stream()
                .map { questionId: Long ->
                    UserQuestion(userId = userId, questionId = questionId)
                }
                .collect(Collectors.toList())
        }
    }
}
