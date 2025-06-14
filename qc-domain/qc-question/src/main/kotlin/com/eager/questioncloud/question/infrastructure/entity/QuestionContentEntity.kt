package com.eager.questioncloud.question.infrastructure.entity

import com.eager.questioncloud.question.domain.QuestionContent
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionType
import com.eager.questioncloud.question.enums.Subject
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
class QuestionContentEntity(
    var questionCategoryId: Long,
    @Enumerated(EnumType.STRING)
    var subject: Subject,
    var title: String,
    var description: String,
    var thumbnail: String,
    var fileUrl: String,
    var explanationUrl: String,
    @Enumerated(EnumType.STRING)
    var questionType: QuestionType,
    @Enumerated(EnumType.STRING)
    var questionLevel: QuestionLevel,
    var price: Int = 0
) {
    fun toModel(): QuestionContent {
        return QuestionContent(
            questionCategoryId,
            subject,
            title,
            description,
            thumbnail,
            fileUrl,
            explanationUrl,
            questionType,
            questionLevel,
            price
        )
    }

    companion object {
        fun from(questionContent: QuestionContent): QuestionContentEntity {
            return QuestionContentEntity(
                questionContent.questionCategoryId,
                questionContent.subject,
                questionContent.title,
                questionContent.description,
                questionContent.thumbnail,
                questionContent.fileUrl,
                questionContent.explanationUrl,
                questionContent.questionType,
                questionContent.questionLevel,
                questionContent.price
            )
        }
    }
}
