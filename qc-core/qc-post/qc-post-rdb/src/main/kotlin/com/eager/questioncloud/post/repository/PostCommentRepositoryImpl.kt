package com.eager.questioncloud.post.repository

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.domain.PostComment
import com.eager.questioncloud.post.entity.PostCommentEntity
import com.eager.questioncloud.post.entity.QPostCommentEntity.postCommentEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

@Repository
class PostCommentRepositoryImpl(
    private val postCommentJpaRepository: PostCommentJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : PostCommentRepository {
    
    override fun save(postComment: PostComment): PostComment {
        return postCommentJpaRepository.save(PostCommentEntity.from(postComment)).toModel()
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
            .offset(pagingInformation.offset.toLong())
            .limit(pagingInformation.size.toLong())
            .fetch()
            .stream()
            .map { it.toModel() }
            .collect(Collectors.toList())
    }
    
    override fun delete(postComment: PostComment) {
        postCommentJpaRepository.delete(PostCommentEntity.from(postComment))
    }
    
    override fun count(postId: Long): Int {
        return jpaQueryFactory.select(postCommentEntity.id.count().intValue())
            .from(postCommentEntity)
            .where(postCommentEntity.postId.eq(postId))
            .fetchFirst() ?: 0
    }
}
