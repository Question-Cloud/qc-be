package com.eager.questioncloud.core.domain.question.infrastructure.entity

import com.eager.questioncloud.core.domain.question.enums.QuestionStatus
import com.eager.questioncloud.core.domain.question.model.Question
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "question")
class QuestionEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long?,
    @Column var creatorId: Long,
    @Embedded var questionContentEntity: QuestionContentEntity,
    @Enumerated(EnumType.STRING) @Column var questionStatus: QuestionStatus,
    @Column var count: Int,
    @Column var createdAt: LocalDateTime
) {
    fun toModel(): Question {
        return Question(
            id,
            creatorId,
            questionContentEntity.toModel(),
            questionStatus,
            count,
            createdAt
        )
    }

    companion object {
        @JvmStatic
        fun from(question: Question): QuestionEntity {
            return QuestionEntity(
                question.id,
                question.creatorId,
                QuestionContentEntity.from(question.questionContent),
                question.questionStatus,
                question.count,
                question.createdAt
            )
        }
    }
}
