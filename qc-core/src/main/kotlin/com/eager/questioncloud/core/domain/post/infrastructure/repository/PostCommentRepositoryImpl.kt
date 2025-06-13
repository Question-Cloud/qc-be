package com.eager.questioncloud.core.domain.post.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.infrastructure.entity.PostCommentEntity.Companion.from
import com.eager.questioncloud.core.domain.post.infrastructure.entity.QPostCommentEntity.postCommentEntity
import com.eager.questioncloud.core.domain.post.model.PostComment
import com.eager.questioncloud.core.domain.user.infrastructure.entity.QUserEntity.userEntity
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

@Repository
class PostCommentRepositoryImpl(
    private val postCommentJpaRepository: PostCommentJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : PostCommentRepository {

    override fun save(postComment: PostComment): PostComment {
        return postCommentJpaRepository.save(from(postComment)).toModel()
    }

    override fun findByIdAndWriterId(commentId: Long, userId: Long): PostComment {
        return postCommentJpaRepository.findByIdAndWriterId(commentId, userId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    override fun findByPostIdWithPagination(
        postId: Long,
        pagingInformation: PagingInformation
    ): List<PostComment> {
        return jpaQueryFactory.select(postCommentEntity)
            .from(postCommentEntity)
            .where(postCommentEntity.postId.eq(postId))
            .leftJoin(userEntity).on(userEntity.uid.eq(postCommentEntity.writerId))
            .offset(pagingInformation.offset.toLong())
            .limit(pagingInformation.size.toLong())
            .fetch()
            .stream()
            .map { it.toModel() }
            .collect(Collectors.toList())
    }

    override fun delete(postComment: PostComment) {
        postCommentJpaRepository.delete(from(postComment))
    }

    override fun count(postId: Long): Int {
        return jpaQueryFactory.select(postCommentEntity.id.count().intValue())
            .from(postCommentEntity)
            .where(postCommentEntity.postId.eq(postId))
            .fetchFirst() ?: 0
    }
}
