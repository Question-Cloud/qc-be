package com.eager.questioncloud.post.api.internal

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.api.CreatorPostQueryAPIResult
import com.eager.questioncloud.post.repository.PostRepository
import org.springframework.stereotype.Component

@Component
class PostQueryAPIImpl(
    private val postRepository: PostRepository,
) : PostQueryAPI {
    override fun getCreatorPosts(
        questionIds: List<Long>,
        pagingInformation: PagingInformation
    ): List<CreatorPostQueryAPIResult> {
        val posts = postRepository.findByQuestionIdInWithPagination(questionIds, pagingInformation)
        return posts.map {
            CreatorPostQueryAPIResult(
                it.id,
                it.writerId,
                it.postContent.title,
                it.postContent.content,
                it.createdAt
            )
        }
    }
    
    override fun countByQuestionIdIn(questionIds: List<Long>): Int {
        return postRepository.countByQuestionIdIn(questionIds)
    }
}