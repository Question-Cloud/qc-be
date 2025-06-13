package com.eager.questioncloud.application.api.workspace.service

import com.eager.questioncloud.application.api.workspace.implement.WorkspacePostReader
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.dto.PostPreview
import org.springframework.stereotype.Component

@Component
class WorkspacePostService(
    private val workspacePostReader: WorkspacePostReader
) {
    fun getCreatorPosts(creatorId: Long, pagingInformation: PagingInformation): List<PostPreview> {
        return workspacePostReader.getCreatorPosts(creatorId, pagingInformation)
    }

    fun countCreatorPost(creatorId: Long): Int {
        return workspacePostReader.countCreatorPost(creatorId)
    }
}
