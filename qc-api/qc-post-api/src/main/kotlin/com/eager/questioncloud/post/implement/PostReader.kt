package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.dto.PostDetail
import com.eager.questioncloud.post.dto.PostPreview
import com.eager.questioncloud.post.infrastructure.repository.PostRepository
import com.eager.questioncloud.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class PostReader(
    private val postPermissionChecker: PostPermissionChecker,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val questionRepository: QuestionRepository,
) {
    fun getPostPreviews(userId: Long, questionId: Long, pagingInformation: PagingInformation): List<PostPreview> {
        if (!postPermissionChecker.hasPermission(userId, questionId)) {
            throw CoreException(Error.FORBIDDEN)
        }
        val posts = postRepository.findByQuestionIdWithPagination(questionId, pagingInformation)
        val writers = userRepository.findByUidIn(posts.map { it.writerId }).associateBy { it.uid }

        val postPreviews = mutableListOf<PostPreview>()

        for (post in posts) {
            val writer = writers.getValue(post.writerId)
            postPreviews.add(
                PostPreview(
                    post.id,
                    post.postContent.title,
                    writer.userInformation.name,
                    post.createdAt,
                )
            )
        }

        return postPreviews
    }

    fun countPost(questionId: Long): Int {
        return postRepository.countByQuestionId(questionId)
    }

    fun getPostDetail(userId: Long, postId: Long): PostDetail {
        val post = postRepository.findById(postId)

        if (!postPermissionChecker.hasPermission(userId, post.questionId)) {
            throw CoreException(Error.FORBIDDEN)
        }

        val question = questionRepository.getQuestionInformation(post.questionId)
        val writer = userRepository.getUser(post.writerId)

        return PostDetail(
            post.id,
            post.questionId,
            post.postContent.title,
            post.postContent.content,
            post.postContent.files,
            question.parentCategory,
            question.childCategory,
            question.title,
            writer.userInformation.name,
            post.createdAt,
        )
    }
}