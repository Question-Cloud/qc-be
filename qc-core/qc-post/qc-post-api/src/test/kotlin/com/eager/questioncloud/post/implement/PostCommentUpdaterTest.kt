package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.post.command.ModifyPostCommentCommand
import com.eager.questioncloud.post.domain.PostComment
import com.eager.questioncloud.post.repository.PostCommentRepository
import com.eager.questioncloud.post.scenario.PostCommentScenario
import com.eager.questioncloud.post.scenario.custom
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class PostCommentUpdaterTest(
    private val postCommentUpdater: PostCommentUpdater,
    private val postCommentRepository: PostCommentRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("댓글 수정") {
            val userId = 1L
            val postId = 1L
            
            val postCommentScenario = PostCommentScenario.create(postId)
            postCommentScenario.postComments[0] = postCommentScenario.postComments[0].custom {
                set(PostComment::writerId, userId)
                set(PostComment::comment, "원본 댓글")
            }
            
            val savedPostComment = postCommentRepository.save(postCommentScenario.postComments[0])
            
            val modifyPostCommentCommand = ModifyPostCommentCommand(
                commentId = savedPostComment.id,
                userId = userId,
                comment = "수정된 댓글"
            )
            
            When("댓글을 수정하면") {
                postCommentUpdater.modify(modifyPostCommentCommand)
                
                Then("댓글이 수정된다") {
                    val updatedPostComment = postCommentRepository.findByIdAndWriterId(savedPostComment.id, modifyPostCommentCommand.userId)
                    updatedPostComment shouldNotBe null
                    updatedPostComment.comment shouldBe "수정된 댓글"
                }
            }
        }
        
        Given("다른 사용자의 댓글 수정 시도") {
            val writerId = 1L
            val otherUserId = 2L
            val postId = 1L
            
            val postCommentScenario = PostCommentScenario.create(postId)
            postCommentScenario.postComments[0] = postCommentScenario.postComments[0].custom {
                set(PostComment::writerId, writerId)
                set(PostComment::comment, "작성자의 댓글")
            }
            
            val savedPostComment = postCommentRepository.save(postCommentScenario.postComments[0])
            
            val modifyPostCommentCommand = ModifyPostCommentCommand(
                commentId = savedPostComment.id,
                userId = otherUserId,
                comment = "타인이 수정하려는 댓글"
            )
            
            When("다른 사용자가 댓글을 수정하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        postCommentUpdater.modify(modifyPostCommentCommand)
                    }
                }
            }
        }
        
        Given("존재하지 않는 댓글 수정 시도") {
            val userId = 1L
            val nonExistentCommentId = 999L
            
            val modifyPostCommentCommand = ModifyPostCommentCommand(
                commentId = nonExistentCommentId,
                userId = userId,
                comment = "존재하지 않는 댓글 수정"
            )
            
            When("존재하지 않는 댓글을 수정하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        postCommentUpdater.modify(modifyPostCommentCommand)
                    }
                }
            }
        }
    }
}