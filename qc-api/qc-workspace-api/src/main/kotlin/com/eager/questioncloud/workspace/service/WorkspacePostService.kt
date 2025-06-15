package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.dto.PostPreview
import com.eager.questioncloud.workspace.implement.WorkspacePostReader
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
