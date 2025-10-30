package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.post.command.DeletePostCommand
import com.eager.questioncloud.post.repository.PostRepository
import com.eager.questioncloud.post.scenario.PostScenario
import com.eager.questioncloud.test.utils.DBCleaner
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
class PostRemoverTest(
    private val postRemover: PostRemover,
    private val postRepository: PostRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("게시글 삭제") {
            val userId = 1L
            val questionId = 1L
            
            val savedPost = postRepository.save(PostScenario.createSinglePost(questionId, userId).posts[0])
            
            val deletePostCommand = DeletePostCommand(
                postId = savedPost.id,
                userId = userId
            )
            
            When("게시글을 삭제하면") {
                postRemover.delete(deletePostCommand)
                
                Then("게시글이 삭제된다") {
                    val postCount = postRepository.countByQuestionId(questionId)
                    postCount shouldBe 0
                }
            }
        }
        
        Given("다른 사용자의 게시글 삭제 시도") {
            val writerId = 1L
            val otherUserId = 2L
            val questionId = 1L
            
            val savedPost = postRepository.save(PostScenario.createSinglePost(questionId, writerId).posts[0])
            
            val deletePostCommand = DeletePostCommand(
                postId = savedPost.id,
                userId = otherUserId
            )
            
            When("다른 사용자가 게시글을 삭제하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        postRemover.delete(deletePostCommand)
                    }
                    
                    val postCount = postRepository.countByQuestionId(questionId)
                    postCount shouldBe 1
                }
            }
        }
        
        Given("존재하지 않는 게시글 삭제 시도") {
            val userId = 1L
            val nonExistentPostId = 999L
            
            val deletePostCommand = DeletePostCommand(
                postId = nonExistentPostId,
                userId = userId
            )
            
            When("존재하지 않는 게시글을 삭제하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        postRemover.delete(deletePostCommand)
                    }
                }
            }
        }
    }
}