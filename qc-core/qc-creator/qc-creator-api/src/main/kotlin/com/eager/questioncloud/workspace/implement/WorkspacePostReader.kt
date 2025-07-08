package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.api.internal.PostQueryAPI
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.workspace.dto.CreatorPostItem
import org.springframework.stereotype.Component

@Component
class WorkspacePostReader(
    private val questionQueryAPI: QuestionQueryAPI,
    private val postQueryAPI: PostQueryAPI,
    private val userQueryAPI: UserQueryAPI
) {
    fun getCreatorPosts(creatorId: Long, pagingInformation: PagingInformation): List<CreatorPostItem> {
        val questions = questionQueryAPI.getCreatorQuestions(creatorId, pagingInformation)
        val posts = postQueryAPI.getCreatorPosts(questions.map { it.id }, pagingInformation)
        val writers = userQueryAPI.getUsers(posts.map { it.writerId }).associateBy { it.userId }

        return posts.map {
            val writer = writers.getValue(it.writerId)
            CreatorPostItem(
                it.id,
                it.title,
                writer.name,
                it.createdAt,
            )
        }
    }

    fun countCreatorPost(creatorId: Long): Int {
        val questions = questionQueryAPI.getCreatorQuestions(creatorId)
        return postQueryAPI.countByQuestionIdIn(questions.map { it.id })
    }
}