package com.eager.questioncloud.post.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.command.DeletePostCommand
import com.eager.questioncloud.post.command.ModifyPostCommand
import com.eager.questioncloud.post.command.RegisterPostCommand
import com.eager.questioncloud.post.domain.Post
import com.eager.questioncloud.post.dto.PostDetail
import com.eager.questioncloud.post.dto.PostPreview
import com.eager.questioncloud.post.implement.PostReader
import com.eager.questioncloud.post.implement.PostRegister
import com.eager.questioncloud.post.implement.PostRemover
import com.eager.questioncloud.post.implement.PostUpdater
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRegister: PostRegister,
    private val postReader: PostReader,
    private val postUpdater: PostUpdater,
    private val postRemover: PostRemover
) {
    fun getPostPreviews(userId: Long, questionId: Long, pagingInformation: PagingInformation): List<PostPreview> {
        return postReader.getPostPreviews(userId, questionId, pagingInformation)
    }
    
    fun countPost(questionId: Long): Int {
        return postReader.countPost(questionId)
    }
    
    fun getPostDetail(userId: Long, postId: Long): PostDetail {
        return postReader.getPostDetail(userId, postId)
    }
    
    fun register(command: RegisterPostCommand): Post {
        return postRegister.register(command)
    }
    
    fun modify(command: ModifyPostCommand) {
        postUpdater.modify(command)
    }
    
    fun delete(command: DeletePostCommand) {
        postRemover.delete(command)
    }
}
