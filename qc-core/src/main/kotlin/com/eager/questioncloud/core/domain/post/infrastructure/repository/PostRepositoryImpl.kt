package com.eager.questioncloud.core.domain.post.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.infrastructure.entity.PostEntity.Companion.from
import com.eager.questioncloud.core.domain.post.infrastructure.entity.QPostEntity.postEntity
import com.eager.questioncloud.core.domain.post.model.Post
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl(
    private val postJpaRepository: PostJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : PostRepository {
    override fun findByQuestionIdWithPagination(questionId: Long, pagingInformation: PagingInformation): List<Post> {
        return jpaQueryFactory.select(postEntity)
            .from(postEntity)
            .where(postEntity.questionId.eq(questionId))
            .offset(pagingInformation.offset.toLong())
            .limit(pagingInformation.size.toLong())
            .fetch()
            .map { it.toModel() }
    }

    override fun findByQuestionIdInWithPagination(
        questionIds: List<Long>,
        pagingInformation: PagingInformation
    ): List<Post> {
        return jpaQueryFactory.select(postEntity)
            .from(postEntity)
            .where(postEntity.questionId.`in`(questionIds))
            .offset(pagingInformation.offset.toLong())
            .limit(pagingInformation.size.toLong())
            .orderBy(postEntity.id.desc())
            .fetch()
            .map { it.toModel() }
    }

    override fun countByQuestionIdIn(questionIds: List<Long>): Int {
        return jpaQueryFactory.select(postEntity.id.count().intValue())
            .from(postEntity)
            .where(postEntity.questionId.`in`(questionIds))
            .fetchFirst() ?: 0
    }

    override fun findByIdAndWriterId(postId: Long, userId: Long): Post {
        return postJpaRepository.findByIdAndWriterId(postId, userId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    override fun findById(postId: Long): Post {
        return postJpaRepository.findById(postId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    override fun countByQuestionId(questionId: Long): Int {
        return jpaQueryFactory.select(postEntity.id.count().intValue())
            .from(postEntity)
            .where(postEntity.questionId.eq(questionId))
            .fetchFirst() ?: 0
    }

    override fun save(post: Post): Post {
        return postJpaRepository.save(from(post)).toModel()
    }

    override fun delete(post: Post) {
        postJpaRepository.delete(from(post))
    }
}
