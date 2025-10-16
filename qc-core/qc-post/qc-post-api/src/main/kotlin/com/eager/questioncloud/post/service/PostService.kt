package com.eager.questioncloud.post.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.command.DeletePostCommand
import com.eager.questioncloud.post.command.ModifyPostCommand
import com.eager.questioncloud.post.command.RegisterPostCommand
import com.eager.questioncloud.post.domain.Post
import com.eager.questioncloud.post.dto.PostDetail
import com.eager.questioncloud.post.dto.PostPreview
import com.eager.questioncloud.post.implement.*
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postPermissionValidator: PostPermissionValidator,
    private val postRegister: PostRegister,
    private val postReader: PostReader,
    private val postUpdater: PostUpdater,
    private val postRemover: PostRemover
) {
    fun getPostPreviews(userId: Long, questionId: Long, pagingInformation: PagingInformation): List<PostPreview> {
        postPermissionValidator.validatePostPermission(userId, questionId)
        return postReader.getPostPreviews(userId, questionId, pagingInformation)
    }
    
    fun countPost(questionId: Long): Int {
        return postReader.countPost(questionId)
    }
    
    fun getPostDetail(userId: Long, postId: Long): PostDetail {
        val postDetail = postReader.getPostDetail(userId, postId)
        postPermissionValidator.validatePostPermission(userId, postDetail.questionId)
        return postDetail
    }
    
    fun register(command: RegisterPostCommand): Post {
        postPermissionValidator.validatePostPermission(command.writerId, command.questionId)
        return postRegister.register(command)
    }
    
    fun modify(command: ModifyPostCommand) {
        postUpdater.modify(command)
    }
    
    fun delete(command: DeletePostCommand) {
        postRemover.delete(command)
    }
}
