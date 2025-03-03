package com.eager.questioncloud.core.domain.review.infrastructure.repository

import com.eager.questioncloud.core.domain.review.infrastructure.entity.QuestionReviewStatisticsEntity.Companion.from
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

    override fun save(questionReviewStatistics: QuestionReviewStatistics): QuestionReviewStatistics {
        return questionReviewStatisticsJpaRepository.save(from(questionReviewStatistics)).toModel()
    }

    override fun deleteAllInBatch() {
        questionReviewStatisticsJpaRepository.deleteAllInBatch()
    }
}
