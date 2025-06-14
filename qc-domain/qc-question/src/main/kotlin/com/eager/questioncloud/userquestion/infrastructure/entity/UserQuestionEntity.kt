package com.eager.questioncloud.userquestion.infrastructure.entity

import com.eager.questioncloud.userquestion.domain.UserQuestion
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.stream.Collectors

@Entity
@Table(name = "user_question")
class UserQuestionEntity private constructor(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long = 0,
    @Column var userId: Long,
    @Column var questionId: Long,
    @Column var isUsed: Boolean,
    @Column var createdAt: LocalDateTime
) {
    fun toModel(): UserQuestion {
        return UserQuestion(id, userId, questionId, isUsed, createdAt)
    }

    companion object {
        fun toModel(userQuestionLibraryEntities: List<UserQuestionEntity>): List<UserQuestion> {
            return userQuestionLibraryEntities
                .stream()
                .map { obj: UserQuestionEntity -> obj.toModel() }
                .collect(Collectors.toList())
        }

        fun from(userQuestionLibraries: List<UserQuestion>): List<UserQuestionEntity> {
            return userQuestionLibraries.stream()
                .map { userQuestion: UserQuestion -> from(userQuestion) }
                .collect(Collectors.toList())
        }

        fun from(userQuestion: UserQuestion): UserQuestionEntity {
            return UserQuestionEntity(
                userQuestion.id,
                userQuestion.userId,
                userQuestion.questionId,
                userQuestion.isUsed,
                userQuestion.createdAt,
            )
        }
    }
}
