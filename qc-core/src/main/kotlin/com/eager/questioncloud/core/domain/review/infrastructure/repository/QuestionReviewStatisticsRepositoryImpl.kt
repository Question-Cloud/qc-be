package com.eager.questioncloud.core.domain.review.infrastructure.repository

import com.eager.questioncloud.core.domain.review.infrastructure.entity.QuestionReviewStatisticsEntity
import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.stereotype.Repository

@Repository
class QuestionReviewStatisticsRepositoryImpl(
    private val questionReviewStatisticsJpaRepository: QuestionReviewStatisticsJpaRepository
) : QuestionReviewStatisticsRepository {

    override fun get(questionId: Long): QuestionReviewStatistics {
        return questionReviewStatisticsJpaRepository.findById(questionId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    override fun getForUpdate(questionId: Long): QuestionReviewStatistics {
        return questionReviewStatisticsJpaRepository.findByQuestionId(questionId)?.toModel()
            ?: throw CoreException(Error.NOT_FOUND)
    }

    override fun save(questionReviewStatistics: QuestionReviewStatistics): QuestionReviewStatistics {
        return questionReviewStatisticsJpaRepository.save(
            QuestionReviewStatisticsEntity.createNewEntity(questionReviewStatistics)
        ).toModel()
    }

    override fun update(questionReviewStatistics: QuestionReviewStatistics) {
        questionReviewStatisticsJpaRepository.save(QuestionReviewStatisticsEntity.fromExisting(questionReviewStatistics))
    }

    override fun deleteAllInBatch() {
        questionReviewStatisticsJpaRepository.deleteAllInBatch()
    }
}
