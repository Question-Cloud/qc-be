package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.post.command.DeletePostCommentCommand
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
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class PostCommentRemoverTest(
    private val postCommentRemover: PostCommentRemover,
    private val postCommentRepository: PostCommentRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterEach {
            dbCleaner.cleanUp()
        }

        Given("댓글 삭제") {
            val userId = 1L
            val postId = 1L

            val postCommentScenario = PostCommentScenario.create(postId)
            postCommentScenario.postComments[0] = postCommentScenario.postComments[0].custom {
                set(PostComment::writerId, userId)
            }

            val savedPostComment = postCommentRepository.save(postCommentScenario.postComments[0])

            val deletePostCommentCommand = DeletePostCommentCommand(
                commentId = savedPostComment.id,
                userId = userId
            )

            When("댓글을 삭제하면") {
                postCommentRemover.deletePostComment(deletePostCommentCommand)

                Then("댓글이 삭제된다") {
                    val commentCount = postCommentRepository.count(postId)
                    commentCount shouldBe 0
                }
            }
        }

        Given("다른 사용자의 댓글 삭제 시도") {
            val writerId = 1L
            val otherUserId = 2L
            val postId = 1L

            val postCommentScenario = PostCommentScenario.create(postId)
            postCommentScenario.postComments[0] = postCommentScenario.postComments[0].custom {
                set(PostComment::writerId, writerId)
            }

            val savedPostComment = postCommentRepository.save(postCommentScenario.postComments[0])

            val deletePostCommentCommand = DeletePostCommentCommand(
                commentId = savedPostComment.id,
                userId = otherUserId
            )

            When("다른 사용자가 댓글을 삭제하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        postCommentRemover.deletePostComment(deletePostCommentCommand)
                    }

                    val commentCount = postCommentRepository.count(postId)
                    commentCount shouldBe 1
                }
            }
        }

        Given("존재하지 않는 댓글 삭제 시도") {
            val userId = 1L
            val nonExistentCommentId = 999L

            val deletePostCommentCommand = DeletePostCommentCommand(
                commentId = nonExistentCommentId,
                userId = userId
            )

            When("존재하지 않는 댓글을 삭제하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        postCommentRemover.deletePostComment(deletePostCommentCommand)
                    }
                }
            }
        }
    }
}