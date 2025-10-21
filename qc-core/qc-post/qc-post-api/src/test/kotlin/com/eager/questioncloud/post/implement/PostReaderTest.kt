package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.repository.PostRepository
import com.eager.questioncloud.post.scenario.PostScenario
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
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
class PostReaderTest(
    private val postReader: PostReader,
    private val postRepository: PostRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    @MockkBean
    private lateinit var userQueryAPI: UserQueryAPI
    
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("게시글 목록 조회") {
            val userId = 1L
            val questionId = 1L
            val postScenario = PostScenario.create(questionId, 2)
            val savedPosts = postScenario.posts.map { postRepository.save(it) }
            
            every { userQueryAPI.getUsers(any()) } returns postScenario.userQueryDatas
            
            val pagingInformation = PagingInformation(0, 10)
            
            When("게시글 목록을 조회하면") {
                val result = postReader.getPostPreviews(userId, questionId, pagingInformation)
                Then("게시글 목록이 반환된다") {
                    result.size shouldBe 2
                    
                    val preview1 = result.find { it.id == savedPosts[0].id }
                    preview1 shouldNotBe null
                    preview1!!.title shouldBe "게시글 제목 1"
                    preview1.writer shouldBe postScenario.userQueryDatas[0].name
                    
                    val preview2 = result.find { it.id == savedPosts[1].id }
                    preview2 shouldNotBe null
                    preview2!!.title shouldBe "게시글 제목 2"
                    preview2.writer shouldBe postScenario.userQueryDatas[1].name
                }
            }
        }
        
        Given("게시글 개수 조회") {
            val questionId = 1L
            val postScenario = PostScenario.create(questionId, 3)
            postScenario.posts.forEach { postRepository.save(it) }
            
            When("게시글 개수를 조회하면") {
                val result = postReader.countPost(questionId)
                Then("게시글 개수가 반환된다") {
                    result shouldBe 3
                }
            }
        }
        
        Given("게시글 상세 정보 조회") {
            val userId = 1L
            val questionId = 1L
            val creatorId = 1L
            val writerId = 2L
            
            val postScenario = PostScenario.createSinglePost(questionId, writerId)
            val savedPost = postRepository.save(postScenario.posts[0])
            
            val questionInformation = QuestionInformationQueryResult(
                id = questionId,
                creatorId = creatorId,
                title = "테스트 문제",
                description = "문제 설명",
                subject = "수학",
                parentCategory = "수학",
                childCategory = "대수",
                thumbnail = "thumbnail.jpg",
                questionLevel = "중급",
                price = 1000,
                rate = 4.5
            )
            
            every { questionQueryAPI.getQuestionInformation(savedPost.questionId) } returns questionInformation
            every { userQueryAPI.getUser(writerId) } returns postScenario.userQueryDatas[0]
            
            When("게시글 상세 정보를 조회하면") {
                val result = postReader.getPostDetail(userId, savedPost.id)
                Then("게시글 상세 정보가 반환된다") {
                    result shouldNotBe null
                    result.id shouldBe savedPost.id
                    result.questionId shouldBe savedPost.questionId
                    result.title shouldBe savedPost.postContent.title
                    result.content shouldBe savedPost.postContent.content
                    result.files.size shouldBe savedPost.postContent.files.size
                    result.parentCategory shouldBe questionInformation.parentCategory
                    result.childCategory shouldBe questionInformation.childCategory
                    result.questionTitle shouldBe questionInformation.title
                    result.writer shouldBe postScenario.userQueryDatas[0].name
                    result.createdAt shouldNotBe null
                }
            }
        }
    }
}