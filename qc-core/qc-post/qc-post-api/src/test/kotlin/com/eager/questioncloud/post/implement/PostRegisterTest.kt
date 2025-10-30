package com.eager.questioncloud.post.implement

import com.eager.questioncloud.post.command.RegisterPostCommand
import com.eager.questioncloud.post.command.RegisterPostCommandPostContent
import com.eager.questioncloud.post.repository.PostRepository
import com.eager.questioncloud.test.utils.DBCleaner
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
class PostRegisterTest(
    private val postRegister: PostRegister,
    private val postRepository: PostRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("게시글 등록") {
            val userId = 1L
            val questionId = 1L
            
            val registerPostCommand = RegisterPostCommand(
                questionId = questionId,
                writerId = userId,
                postContent = RegisterPostCommandPostContent(
                    title = "테스트 게시글",
                    content = "게시글 내용입니다",
                    files = emptyList()
                )
            )
            
            When("게시글을 등록하면") {
                val result = postRegister.register(registerPostCommand)
                
                Then("게시글이 등록된다") {
                    result shouldNotBe null
                    result.questionId shouldBe questionId
                    result.writerId shouldBe userId
                    result.postContent.title shouldBe registerPostCommand.postContent.title
                    result.postContent.content shouldBe registerPostCommand.postContent.content
                    
                    val savedPost = postRepository.findById(result.id)
                    savedPost shouldNotBe null
                    savedPost.questionId shouldBe questionId
                    savedPost.writerId shouldBe userId
                }
            }
        }
    }
}