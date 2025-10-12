package com.eager.questioncloud.post.implement

import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.creator.api.internal.CreatorQueryData
import com.eager.questioncloud.post.domain.Post
import com.eager.questioncloud.post.domain.PostContent
import com.eager.questioncloud.post.repository.PostRepository
import com.eager.questioncloud.post.scenario.PostScenario
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class PostPermissionCheckerTest(
    private val postRepository: PostRepository,
    private val postPermissionChecker: PostPermissionChecker,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    @MockkBean
    private lateinit var creatorQueryAPI: CreatorQueryAPI
    
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("문제 크리에이터가 문제 게시글 접근 권한 확인") {
            val questionId = 1L
            val creatorId = 1L
            
            val requestUserId = 1L
            
            val questionInformation = createQuestionInformation(questionId, creatorId)
            every { questionQueryAPI.getQuestionInformation(questionId) } returns questionInformation
            
            val creatorQueryData = createCreatorQueryData(requestUserId, creatorId)
            every { creatorQueryAPI.getCreator(creatorId) } returns creatorQueryData
            
            When("문제 크리에이터가 게시글 접근 권한을 확인하면") {
                val hasPermission = postPermissionChecker.hasPermission(requestUserId, questionId)
                Then("권한이 있다") {
                    hasPermission shouldBe true
                }
            }
        }
        
        Given("문제 구매자 게시글 접근 권한 확인") {
            val questionId = 1L
            val creatorId = 2L
            val creatorUserId = 2L
            
            val requestUserId = 1L
            
            val questionInformation = createQuestionInformation(questionId, creatorId)
            every { questionQueryAPI.getQuestionInformation(questionId) } returns questionInformation
            
            val creatorQueryData = createCreatorQueryData(creatorUserId, creatorId)
            every { creatorQueryAPI.getCreator(creatorId) } returns creatorQueryData
            
            every { questionQueryAPI.isOwned(requestUserId, questionId) } returns true
            
            When("문제를 소유한 사용자가 게시글 접근 권한을 확인하면") {
                val hasPermission = postPermissionChecker.hasPermission(requestUserId, questionId)
                Then("권한이 있다") {
                    hasPermission shouldBe true
                }
            }
        }
        
        Given("권한이 없는 사용자 확인") {
            val questionId = 1L
            val creatorId = 2L
            val creatorUserId = 3L
            
            val requestUserId = 1L
            
            val questionInformation = createQuestionInformation(questionId, creatorId)
            every { questionQueryAPI.getQuestionInformation(questionId) } returns questionInformation
            
            val creatorQueryData = createCreatorQueryData(creatorUserId, creatorId)
            every { creatorQueryAPI.getCreator(creatorId) } returns creatorQueryData
            
            every { questionQueryAPI.isOwned(requestUserId, questionId) } returns false
            
            When("크리에이터도 아니고 문제를 소유하지도 않은 사용자가 게시글 접근 권한을 확인하면") {
                val hasPermission = postPermissionChecker.hasPermission(requestUserId, questionId)
                Then("권한이 없다") {
                    hasPermission shouldBe false
                }
            }
        }
        
        Given("댓글 권한 확인 - 권한이 있는 경우") {
            val postWriterId = 1L
            val questionId = 1L
            val postId = 1L
            val creatorId = 1L
            
            val requestUserId = 1L
            
            val postScenario = PostScenario.createSinglePost(questionId, postWriterId)
            postRepository.save(postScenario.posts[0])
            
            val questionInformation = createQuestionInformation(questionId, creatorId)
            every { questionQueryAPI.getQuestionInformation(questionId) } returns questionInformation
            
            val creatorQueryData = createCreatorQueryData(requestUserId, creatorId)
            every { creatorQueryAPI.getCreator(creatorId) } returns creatorQueryData
            
            When("해당 포스트의 문제에 대한 권한이 있는 사용자가 댓글 권한을 확인하면") {
                val hasCommentPermission = postPermissionChecker.hasCommentPermission(requestUserId, postId)
                Then("댓글 권한이 있다") {
                    hasCommentPermission shouldBe true
                }
            }
        }
        
        Given("댓글 권한 확인 - 권한이 없는 경우") {
            val postWriterId = 1L
            val questionId = 1L
            val postId = 1L
            val creatorId = 2L
            val creatorUserId = 3L
            
            val requestUserId = 1L
            
            val postScenario = PostScenario.createSinglePost(questionId, postWriterId)
            postRepository.save(postScenario.posts[0])
            
            val questionInformation = createQuestionInformation(questionId, creatorId)
            every { questionQueryAPI.getQuestionInformation(questionId) } returns questionInformation
            
            val creatorQueryData = createCreatorQueryData(creatorUserId, creatorId)
            every { creatorQueryAPI.getCreator(creatorId) } returns creatorQueryData
            
            every { questionQueryAPI.isOwned(requestUserId, questionId) } returns false
            
            When("해당 포스트의 문제에 대한 권한이 없는 사용자가 댓글 권한을 확인하면") {
                val hasCommentPermission = postPermissionChecker.hasCommentPermission(requestUserId, postId)
                Then("댓글 권한이 없다") {
                    hasCommentPermission shouldBe false
                }
            }
        }
        
        Given("문제 크리에이터 여부 확인") {
            val questionId = 1L
            val creatorId = 1L
            val notCreatorUserId = 1L
            val creatorUserId = 2L
            
            val questionInformation = createQuestionInformation(questionId, creatorId)
            every { questionQueryAPI.getQuestionInformation(questionId) } returns questionInformation
            
            val creatorQueryData = createCreatorQueryData(creatorUserId, creatorId)
            every { creatorQueryAPI.getCreator(creatorId) } returns creatorQueryData
            
            When("문제를 만든 크리에이터인지 확인을 하면") {
                val isCreator = postPermissionChecker.isCreator(creatorUserId, questionId)
                val isNotCreator = postPermissionChecker.isCreator(notCreatorUserId, questionId)
                
                Then("크리에이터 확인이 정확하게 동작한다") {
                    isCreator shouldBe true
                    isNotCreator shouldBe false
                }
            }
        }
    }
    
    private fun createQuestionInformation(questionId: Long, creatorId: Long): QuestionInformationQueryResult {
        return QuestionInformationQueryResult(
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
    }
    
    private fun createCreatorQueryData(userId: Long, creatorId: Long): CreatorQueryData {
        return CreatorQueryData(
            userId = userId,
            creatorId = creatorId,
            mainSubject = "수학",
            rate = 4.5,
            sales = 100,
            subscriberCount = 500
        )
    }
    
    private fun createMockPost(questionId: Long, writerId: Long, content: String): Post {
        val postContent = PostContent.create("테스트 제목", content, emptyList())
        return Post.create(questionId, writerId, postContent)
    }
}