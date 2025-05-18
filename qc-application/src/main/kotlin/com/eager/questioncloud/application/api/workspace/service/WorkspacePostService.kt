package com.eager.questioncloud.application.api.workspace.service

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.dto.PostListItem
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostRepository
import org.springframework.stereotype.Component

@Component
class WorkspacePostService(
    private val postRepository: PostRepository
) {
    fun getCreatorPosts(creatorId: Long, pagingInformation: PagingInformation): List<PostListItem> {
        return postRepository.getCreatorPostList(creatorId, pagingInformation)
    }

    fun countCreatorPost(creatorId: Long): Int {
        return postRepository.countCreatorPost(creatorId)
    }
}
