package com.eager.questioncloud.application.api.workspace.implement

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.dto.PostPreview
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostRepository
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class WorkspacePostReader(
    private val questionRepository: QuestionRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    fun getCreatorPosts(creatorId: Long, pagingInformation: PagingInformation): List<PostPreview> {
        val questions = questionRepository.findByCreatorId(creatorId)
        val posts = postRepository.findByQuestionIdInWithPagination(questions.map { it.id }, pagingInformation)
        val writers = userRepository.findByUidIn(posts.map { it.writerId }).associateBy { it.uid }

        return posts.map {
            val writer = writers.getValue(it.writerId)
            PostPreview(
                it.id,
                it.postContent.title,
                writer.userInformation.name,
                it.createdAt,
            )
        }
    }

    fun countCreatorPost(creatorId: Long): Int {
        val questions = questionRepository.findByCreatorId(creatorId)
        return postRepository.countByQuestionIdIn(questions.map { it.id })
    }
}