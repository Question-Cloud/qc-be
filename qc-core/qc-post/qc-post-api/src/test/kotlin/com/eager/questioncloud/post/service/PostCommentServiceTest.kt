package com.eager.questioncloud.post.service

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.command.DeletePostCommentCommand
import com.eager.questioncloud.post.command.ModifyPostCommentCommand
import com.eager.questioncloud.post.command.RegisterPostCommentCommand
import com.eager.questioncloud.post.domain.PostComment
import com.eager.questioncloud.post.dto.PostCommentDetail
import com.eager.questioncloud.post.implement.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

class PostCommentServiceTest : BehaviorSpec() {
    private val postPermissionValidator = mockk<PostPermissionValidator>()
    private val postCommentRegister = mockk<PostCommentRegister>()
    private val postCommentUpdater = mockk<PostCommentUpdater>()
    private val postCommentReader = mockk<PostCommentReader>()
    private val postCommentRemover = mockk<PostCommentRemover>()
    
    private val postCommentService = PostCommentService(
        postPermissionValidator,
        postCommentRegister,
        postCommentUpdater,
        postCommentReader,
        postCommentRemover
    )
    
    init {
        Given("댓글 등록") {
            val postId = 1L
            val userId = 1L
            val comment = "테스트 댓글입니다"
            
            val registerPostCommentCommand = RegisterPostCommentCommand(
                postId = postId,
                userId = userId,
                comment = comment
            )
            
            val savedPost = PostComment.create(postId, userId, comment, true)
            
            every { postCommentRegister.register(registerPostCommentCommand) } returns savedPost
            justRun { postPermissionValidator.validateCommentPermission(any(), any()) }
            
            When("댓글을 등록하면") {
                postCommentService.addPostComment(registerPostCommentCommand)
                
                Then("댓글이 등록된다") {
                    verify(exactly = 1) { postCommentRegister.register(registerPostCommentCommand) }
                }
            }
        }
        
        Given("댓글 수정") {
            val commentId = 1L
            val userId = 1L
            val modifiedComment = "수정된 댓글"
            
            val modifyPostCommentCommand = ModifyPostCommentCommand(
                commentId = commentId,
                userId = userId,
                comment = modifiedComment
            )
            
            justRun { postCommentUpdater.modify(modifyPostCommentCommand) }
            
            When("댓글을 수정하면") {
                postCommentService.modifyPostComment(modifyPostCommentCommand)
                
                Then("댓글이 수정된다") {
                    verify(exactly = 1) { postCommentUpdater.modify(modifyPostCommentCommand) }
                }
            }
        }
        
        Given("댓글 삭제") {
            val commentId = 1L
            val userId = 1L
            
            val deletePostCommentCommand = DeletePostCommentCommand(
                commentId = commentId,
                userId = userId
            )
            
            justRun { postCommentRemover.deletePostComment(deletePostCommentCommand) }
            
            When("댓글을 삭제하면") {
                postCommentService.deletePostComment(deletePostCommentCommand)
                
                Then("댓글이 삭제된다") {
                    verify(exactly = 1) { postCommentRemover.deletePostComment(deletePostCommentCommand) }
                }
            }
        }
        
        Given("댓글 목록 조회") {
            val postId = 1L
            val userId = 1L
            val pagingInformation = PagingInformation(0, 10)
            
            val postCommentDetails = listOf(
                PostCommentDetail(
                    id = 1L,
                    writerName = "작성자1",
                    profileImage = "profile1.jpg",
                    comment = "첫 번째 댓글",
                    isCreator = false,
                    isWriter = false,
                    createdAt = LocalDateTime.now()
                ),
                PostCommentDetail(
                    id = 2L,
                    writerName = "작성자2",
                    profileImage = "profile2.jpg",
                    comment = "두 번째 댓글",
                    isCreator = false,
                    isWriter = false,
                    createdAt = LocalDateTime.now()
                )
            )
            
            every { postCommentReader.getPostCommentDetails(userId, postId, pagingInformation) } returns postCommentDetails
            justRun { postPermissionValidator.validateCommentPermission(any(), any()) }
            
            When("댓글 목록을 조회하면") {
                val result = postCommentService.getPostCommentDetails(postId, userId, pagingInformation)
                
                Then("댓글 목록이 반환된다") {
                    result.size shouldBe 2
                    
                    val commentDetail1 = result.find { it.id == postCommentDetails[0].id }!!
                    commentDetail1.writerName shouldBe postCommentDetails[0].writerName
                    commentDetail1.profileImage shouldBe postCommentDetails[0].profileImage
                    
                    val commentDetail2 = result.find { it.id == postCommentDetails[1].id }!!
                    commentDetail2.writerName shouldBe postCommentDetails[1].writerName
                    commentDetail2.profileImage shouldBe postCommentDetails[1].profileImage
                    
                    verify(exactly = 1) { postCommentReader.getPostCommentDetails(userId, postId, pagingInformation) }
                }
            }
        }
        
        Given("댓글 개수 조회") {
            val postId = 1L
            
            every { postCommentReader.count(postId) } returns 3
            
            When("댓글 개수를 조회하면") {
                val result = postCommentService.count(postId)
                
                Then("댓글 개수가 반환된다") {
                    result shouldBe 3
                    
                    verify(exactly = 1) { postCommentReader.count(postId) }
                }
            }
        }
        
        Given("권한이 없을 때") {
            val postId = 1L
            val userId = 1L
            val comment = "테스트 댓글입니다"
            
            val registerPostCommentCommand = RegisterPostCommentCommand(
                postId = postId,
                userId = userId,
                comment = comment
            )
            
            every { postPermissionValidator.validateCommentPermission(any(), any()) } throws CoreException(Error.FORBIDDEN)
            
            When("댓글을 등록하면") {
                Then("예외가 발생한다.") {
                    shouldThrow<CoreException> {
                        postCommentService.addPostComment(registerPostCommentCommand)
                    }
                }
            }
            
            When("댓글 목록을 조회하면") {
                Then("예외가 발생한다.") {
                    shouldThrow<CoreException> {
                        postCommentService.getPostCommentDetails(postId, userId, PagingInformation.max)
                    }
                }
            }
        }
        
    }
}