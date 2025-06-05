package com.eager.questioncloud.core.domain.post.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.creator.infrastructure.entity.QCreatorEntity.creatorEntity
import com.eager.questioncloud.core.domain.post.dto.PostCommentDetail
import com.eager.questioncloud.core.domain.post.infrastructure.entity.PostCommentEntity.Companion.from
import com.eager.questioncloud.core.domain.post.infrastructure.entity.QPostCommentEntity.postCommentEntity
import com.eager.questioncloud.core.domain.post.infrastructure.entity.QPostEntity.postEntity
import com.eager.questioncloud.core.domain.post.model.PostComment
import com.eager.questioncloud.core.domain.question.infrastructure.entity.QQuestionEntity.questionEntity
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

    override fun getPostCommentDetails(
        postId: Long,
        userId: Long,
        pagingInformation: PagingInformation
    ): List<PostCommentDetail> {
        val questionCreatorUserId = getQuestionCreatorUserId(postId)
        return jpaQueryFactory.select(
            postCommentEntity.id,
            userEntity.uid,
            userEntity.userInformationEntity.name,
            userEntity.userInformationEntity.profileImage,
            postCommentEntity.comment,
            postCommentEntity.createdAt
        )
            .from(postCommentEntity)
            .where(postCommentEntity.postId.eq(postId))
            .leftJoin(userEntity).on(userEntity.uid.eq(postCommentEntity.writerId))
            .offset(pagingInformation.offset.toLong())
            .limit(pagingInformation.size.toLong())
            .fetch()
            .stream()
            .map { tuple ->
                PostCommentDetail(
                    tuple.get(postCommentEntity.id),
                    tuple.get(userEntity.userInformationEntity.name),
                    tuple.get(userEntity.userInformationEntity.profileImage),
                    tuple.get(postCommentEntity.comment),
                    questionCreatorUserId == tuple.get(userEntity.uid),
                    userId == tuple.get(userEntity.uid),
                    tuple.get(postCommentEntity.createdAt)
                )
            }
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

    private fun getQuestionCreatorUserId(postId: Long): Long {
        return jpaQueryFactory.select(userEntity.uid)
            .from(postEntity)
            .innerJoin(questionEntity).on(questionEntity.id.eq(postEntity.questionId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .where(postEntity.id.eq(postId))
            .fetchFirst() ?: throw CoreException(Error.NOT_FOUND)
    }
}
