package com.eager.questioncloud.post.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.command.DeletePostCommentCommand
import com.eager.questioncloud.post.command.ModifyPostCommentCommand
import com.eager.questioncloud.post.command.RegisterPostCommentCommand
import com.eager.questioncloud.post.dto.PostCommentDetail
import com.eager.questioncloud.post.implement.*
import org.springframework.stereotype.Service

@Service
class PostCommentService(
    private val postPermissionValidator: PostPermissionValidator,
    private val postCommentRegister: PostCommentRegister,
    private val postCommentUpdater: PostCommentUpdater,
    private val postCommentReader: PostCommentReader,
    private val postCommentRemover: PostCommentRemover,
) {
    fun addPostComment(command: RegisterPostCommentCommand) {
        postPermissionValidator.validateCommentPermission(command.userId, command.postId)
        postCommentRegister.register(command)
    }
    
    fun getPostCommentDetails(
        postId: Long,
        userId: Long,
        pagingInformation: PagingInformation
    ): List<PostCommentDetail> {
        postPermissionValidator.validateCommentPermission(userId, postId)
        return postCommentReader.getPostCommentDetails(userId, postId, pagingInformation)
    }
    
    fun count(postId: Long): Int {
        return postCommentReader.count(postId)
    }
    
    fun modifyPostComment(command: ModifyPostCommentCommand) {
        postCommentUpdater.modify(command)
    }
    
    fun deletePostComment(command: DeletePostCommentCommand) {
        postCommentRemover.deletePostComment(command)
    }
}
