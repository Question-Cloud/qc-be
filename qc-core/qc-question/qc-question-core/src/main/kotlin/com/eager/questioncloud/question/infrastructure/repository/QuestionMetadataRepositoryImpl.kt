package com.eager.questioncloud.question.infrastructure.repository

import com.eager.questioncloud.question.domain.QuestionMetadata
import com.eager.questioncloud.question.infrastructure.entity.QQuestionMetadataEntity.questionMetadataEntity
import com.eager.questioncloud.question.infrastructure.entity.QuestionMetadataEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class QuestionMetadataRepositoryImpl(
    private val questionMetadataJpaRepository: QuestionMetadataJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : QuestionMetadataRepository {
    override fun save(questionMetadata: QuestionMetadata): QuestionMetadata {
        return questionMetadataJpaRepository.save(QuestionMetadataEntity.createNewEntity(questionMetadata)).toModel()
    }

    override fun update(questionMetadata: QuestionMetadata): QuestionMetadata {
        return questionMetadataJpaRepository.save(QuestionMetadataEntity.fromExisting(questionMetadata)).toModel()
    }

    @Transactional
    override fun increaseSales(questionId: Long) {
        jpaQueryFactory.update(questionMetadataEntity)
            .set(questionMetadataEntity.sales, questionMetadataEntity.sales.add(1))
            .where(questionMetadataEntity.questionId.eq(questionId))
            .execute()
    }

    @Transactional
    override fun getForUpdate(questionId: Long): QuestionMetadata {
        return questionMetadataJpaRepository.findByQuestionId(questionId).toModel()
    }
}