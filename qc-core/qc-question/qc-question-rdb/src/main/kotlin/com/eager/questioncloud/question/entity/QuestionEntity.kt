package com.eager.questioncloud.question.entity

import com.eager.questioncloud.question.domain.Question
import com.eager.questioncloud.question.enums.QuestionStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "question")
class QuestionEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long = 0,
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
