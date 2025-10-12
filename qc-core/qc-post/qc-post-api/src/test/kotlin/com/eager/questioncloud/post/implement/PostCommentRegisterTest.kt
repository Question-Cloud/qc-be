package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.post.command.RegisterPostCommentCommand
import com.eager.questioncloud.post.repository.PostCommentRepository
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class PostCommentRegisterTest(
    private val postCommentRegister: PostCommentRegister,
    private val postCommentRepository: PostCommentRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var postPermissionChecker: PostPermissionChecker
    
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("일반 사용자의 댓글 등록") {
            val userId = 1L
            val postId = 1L
            val questionId = 1L
            val creatorId = 2L
            val comment = "좋은 문제네요!"
            
            val registerPostCommentCommand = RegisterPostCommentCommand(
                postId = postId,
                userId = userId,
                comment = comment
            )
            
            every { postPermissionChecker.hasCommentPermission(userId, postId) } returns true
            every { postPermissionChecker.isCreator(userId, questionId) } returns false
            
            val questionInformation = QuestionInformationQueryResult(
                id = questionId,
                creatorId = creatorId,
                title = "테스트 문제",
                subject = "수학",
                parentCategory = "수학",
                childCategory = "대수",
                thumbnail = "thumbnail.jpg",
                questionLevel = "중급",
                price = 1000,
                rate = 4.5
            )
            every { questionQueryAPI.getQuestionInformation(postId) } returns questionInformation
            
            When("댓글을 등록하면") {
                val result = postCommentRegister.register(registerPostCommentCommand)
                
                Then("일반 사용자 댓글이 등록된다") {
                    result shouldNotBe null
                    result.postId shouldBe postId
                    result.writerId shouldBe userId
                    result.comment shouldBe comment
                    result.isCreator shouldBe false
                    
                    val commentCount = postCommentRepository.count(postId)
                    commentCount shouldBe 1
                }
            }
        }
        
        Given("크리에이터의 댓글 등록") {
            val userId = 1L
            val postId = 1L
            val questionId = 1L
            val creatorId = 1L
            val comment = "문제에 대한 부가 설명입니다."
            
            val registerPostCommentCommand = RegisterPostCommentCommand(
                postId = postId,
                userId = userId,
                comment = comment
            )
            
            every { postPermissionChecker.hasCommentPermission(userId, postId) } returns true
            every { postPermissionChecker.isCreator(userId, questionId) } returns true
            
            val questionInformation = QuestionInformationQueryResult(
                id = questionId,
                creatorId = creatorId,
                title = "테스트 문제",
                subject = "수학",
                parentCategory = "수학",
                childCategory = "대수",
                thumbnail = "thumbnail.jpg",
                questionLevel = "중급",
                price = 1000,
                rate = 4.5
            )
            every { questionQueryAPI.getQuestionInformation(postId) } returns questionInformation
            
            When("크리에이터가 댓글을 등록하면") {
                val result = postCommentRegister.register(registerPostCommentCommand)
                
                Then("크리에이터 댓글이 등록된다") {
                    result shouldNotBe null
                    result.postId shouldBe postId
                    result.writerId shouldBe userId
                    result.comment shouldBe comment
                    result.isCreator shouldBe true
                    
                    val commentCount = postCommentRepository.count(postId)
                    commentCount shouldBe 1
                }
            }
        }
        
        Given("댓글 작성 권한이 없는 사용자의 댓글 등록 시도") {
            val userId = 1L
            val postId = 1L
            val comment = "권한 없이 작성하려는 댓글"
            
            val registerPostCommentCommand = RegisterPostCommentCommand(
                postId = postId,
                userId = userId,
                comment = comment
            )
            
            every { postPermissionChecker.hasCommentPermission(userId, postId) } returns false
            
            When("댓글을 등록하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        postCommentRegister.register(registerPostCommentCommand)
                    }
                }
            }
        }
    }
}