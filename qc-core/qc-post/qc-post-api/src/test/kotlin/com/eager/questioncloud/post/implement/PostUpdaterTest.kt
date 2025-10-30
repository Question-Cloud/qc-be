package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.post.command.ModifyPostCommand
import com.eager.questioncloud.post.command.ModifyPostCommandPostContent
import com.eager.questioncloud.post.command.ModifyPostCommandPostFile
import com.eager.questioncloud.post.repository.PostRepository
import com.eager.questioncloud.post.scenario.PostScenario
import com.eager.questioncloud.test.utils.DBCleaner
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
class PostUpdaterTest(
    private val postUpdater: PostUpdater,
    private val postRepository: PostRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("게시글 수정") {
            val userId = 1L
            val questionId = 1L
            
            val originPost = postRepository.save(PostScenario.createSinglePost(questionId, userId).posts[0])
            
            val modifyPostCommand = ModifyPostCommand(
                postId = originPost.id,
                userId = userId,
                ModifyPostCommandPostContent(
                    title = "수정된 제목",
                    content = "수정된 내용",
                    files = listOf(
                        ModifyPostCommandPostFile("test.txt", "https://example.com/test.txt")
                    )
                )
            )
            
            When("게시글을 수정하면") {
                postUpdater.modify(modifyPostCommand)
                
                Then("게시글이 수정된다") {
                    val updatedPost = postRepository.findById(originPost.id)
                    updatedPost shouldNotBe null
                    updatedPost.postContent.title shouldBe modifyPostCommand.postContent.title
                    updatedPost.postContent.content shouldBe modifyPostCommand.postContent.content
                    updatedPost.postContent.files.size shouldBe 1
                    updatedPost.postContent.files[0].fileName shouldBe modifyPostCommand.postContent.files[0].fileName
                }
            }
        }
        
        Given("다른 사용자의 게시글 수정 시도") {
            val userId = 1L
            val otherUserId = 2L
            val questionId = 1L
            
            val otherUserPost = postRepository.save(PostScenario.createSinglePost(questionId, otherUserId).posts[0])
            
            val modifyPostCommand = ModifyPostCommand(
                postId = otherUserPost.id,
                userId = userId,
                ModifyPostCommandPostContent(
                    title = "타인이 수정하려는 제목",
                    content = "타인이 수정하려는 내용",
                    files = emptyList()
                )
            )
            
            When("다른 사용자가 게시글을 수정하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        postUpdater.modify(modifyPostCommand)
                    }
                }
            }
        }
    }
}