package com.eager.questioncloud.question.infrastructure.entity

import com.eager.questioncloud.entity.BaseCustomIdEntity
import com.eager.questioncloud.question.domain.QuestionMetadata
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "question_metadata")
class QuestionMetadataEntity(
    @Id var questionId: Long,
    @Column val sales: Int,
    @Column val reviewCount: Int,
    @Column val reviewAverageRate: Double,
    isNewEntity: Boolean
) : BaseCustomIdEntity<Long>(isNewEntity) {

    companion object {
        fun createNewEntity(questionMetadata: QuestionMetadata): QuestionMetadataEntity {
            return QuestionMetadataEntity(
                questionMetadata.questionId,
                questionMetadata.sales,
                questionMetadata.reviewCount,
                questionMetadata.reviewAverageRate,
                true
            )
        }

        fun fromExisting(questionMetadata: QuestionMetadata): QuestionMetadataEntity {
            return QuestionMetadataEntity(
                questionMetadata.questionId,
                questionMetadata.sales,
                questionMetadata.reviewCount,
                questionMetadata.reviewAverageRate,
                false
            )
        }
    }

    fun toModel(): QuestionMetadata {
        return QuestionMetadata(questionId, sales, reviewCount, reviewAverageRate)
    }

    override fun getId(): Long {
        return questionId
    }
}