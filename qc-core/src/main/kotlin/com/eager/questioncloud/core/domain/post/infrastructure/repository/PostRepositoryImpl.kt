package com.eager.questioncloud.core.domain.post.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.dto.PostDetail
import com.eager.questioncloud.core.domain.post.dto.PostListItem
import com.eager.questioncloud.core.domain.post.infrastructure.entity.PostEntity.Companion.from
import com.eager.questioncloud.core.domain.post.infrastructure.entity.QPostEntity.postEntity
import com.eager.questioncloud.core.domain.post.model.Post
import com.eager.questioncloud.core.domain.question.infrastructure.entity.QQuestionCategoryEntity
import com.eager.questioncloud.core.domain.question.infrastructure.entity.QQuestionEntity.questionEntity
import com.eager.questioncloud.core.domain.user.infrastructure.entity.QUserEntity.userEntity
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl(
    private val postJpaRepository: PostJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : PostRepository {
    private val parent = QQuestionCategoryEntity("parent")
    private val child = QQuestionCategoryEntity("child")

    override fun getPostList(questionId: Long, pagingInformation: PagingInformation): List<PostListItem> {
        return jpaQueryFactory.select(
            Projections.constructor(
                PostListItem::class.java,
                postEntity.id,
                postEntity.postContentEntity.title,
                userEntity.userInformationEntity.name,
                postEntity.createdAt
            )
        )
            .from(postEntity)
            .where(postEntity.questionId.eq(questionId))
            .leftJoin(userEntity).on(userEntity.uid.eq(postEntity.writerId))
            .offset(pagingInformation.offset.toLong())
            .limit(pagingInformation.size.toLong())
            .fetch()
    }

    override fun getCreatorPostList(creatorId: Long, pagingInformation: PagingInformation): List<PostListItem> {
        return jpaQueryFactory.select(
            Projections.constructor(
                PostListItem::class.java,
                postEntity.id,
                postEntity.postContentEntity.title,
                userEntity.userInformationEntity.name,
                postEntity.createdAt
            )
        )
            .from(questionEntity)
            .where(questionEntity.creatorId.eq(creatorId))
            .innerJoin(postEntity).on(postEntity.questionId.eq(questionEntity.id))
            .innerJoin(userEntity).on(userEntity.uid.eq(postEntity.writerId))
            .offset(pagingInformation.offset.toLong())
            .limit(pagingInformation.size.toLong())
            .fetch()
    }

    override fun countCreatorPost(creatorId: Long): Int {
        return jpaQueryFactory.select(postEntity.id.count().intValue())
            .from(questionEntity)
            .where(questionEntity.creatorId.eq(creatorId))
            .innerJoin(postEntity)
            .on(postEntity.questionId.eq(questionEntity.id))
            .fetchFirst() ?: 0
    }

    override fun getPostDetail(postId: Long): PostDetail {
        return jpaQueryFactory.select(
            Projections.constructor(
                PostDetail::class.java,
                postEntity.id,
                postEntity.questionId,
                postEntity.postContentEntity.title,
                postEntity.postContentEntity.content,
                postEntity.postContentEntity.files,
                parent.title,
                child.title,
                questionEntity.questionContentEntity.title,
                userEntity.userInformationEntity.name,
                postEntity.createdAt
            )
        )
            .from(postEntity)
            .where(postEntity.id.eq(postId))
            .leftJoin(questionEntity)
            .on(questionEntity.id.eq(postEntity.questionId))
            .leftJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .leftJoin(userEntity).on(userEntity.uid.eq(postEntity.writerId))
            .fetchFirst() ?: throw CoreException(Error.NOT_FOUND)
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

    override fun count(questionId: Long): Int {
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
