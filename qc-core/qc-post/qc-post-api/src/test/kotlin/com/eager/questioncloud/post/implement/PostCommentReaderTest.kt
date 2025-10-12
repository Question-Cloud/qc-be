package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.domain.PostComment
import com.eager.questioncloud.post.repository.PostCommentRepository
import com.eager.questioncloud.post.scenario.PostCommentScenario
import com.eager.questioncloud.post.scenario.custom
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
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
class PostCommentReaderTest(
    private val postCommentReader: PostCommentReader,
    private val postCommentRepository: PostCommentRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var postPermissionChecker: PostPermissionChecker
    
    @MockkBean
    private lateinit var userQueryAPI: UserQueryAPI
    
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("댓글 목록 조회") {
            val userId = 1L
            val postId = 1L
            
            val postCommentScenario = PostCommentScenario.create(postId)
            val savedPostComments = postCommentScenario.postComments.map { postCommentRepository.save(it) }
            
            every { postPermissionChecker.hasCommentPermission(userId, postId) } returns true
            
            every { userQueryAPI.getUsers(any()) } returns postCommentScenario.userQueryDatas
            
            val pagingInformation = PagingInformation(0, 10)
            
            When("댓글 목록을 조회하면") {
                val result = postCommentReader.getPostCommentDetails(userId, postId, pagingInformation)
                
                Then("댓글 목록이 반환된다") {
                    result.size shouldBe postCommentScenario.postComments.size
                    
                    result.forEach { postComment ->
                        val expectedPostComment = savedPostComments.find { it.id == postComment.id }!!
                        postComment.comment shouldBe expectedPostComment.comment
                    }
                }
            }
        }
        
        Given("자신이 작성한 댓글 조회") {
            val userId = 1L
            val postId = 1L
            
            val postCommentScenario = PostCommentScenario.create(postId)
            postCommentScenario.postComments[0] = postCommentScenario.postComments[0].custom {
                set(PostComment::writerId, 1L)
            }
            
            val savedPostComments = postCommentScenario.postComments.map { postCommentRepository.save(it) }
            
            val savedMyComment = savedPostComments[0]
            
            every { postPermissionChecker.hasCommentPermission(userId, postId) } returns true
            every { userQueryAPI.getUsers(any()) } returns postCommentScenario.userQueryDatas
            
            val pagingInformation = PagingInformation.max
            
            When("댓글 목록을 조회하면") {
                val result = postCommentReader.getPostCommentDetails(userId, postId, pagingInformation)
                
                Then("자신의 댓글은 isWriter가 true이다") {
                    val myComment = result.find { it.id == savedMyComment.id }!!
                    myComment.isWriter shouldBe true
                }
            }
        }
        
        Given("댓글 조회 권한이 없는 사용자의 댓글 조회 시도") {
            val userId = 1L
            val postId = 1L
            
            every { postPermissionChecker.hasCommentPermission(userId, postId) } returns false
            
            val pagingInformation = PagingInformation(0, 10)
            
            When("댓글 목록을 조회하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        postCommentReader.getPostCommentDetails(userId, postId, pagingInformation)
                    }
                }
            }
        }
        
        Given("크리에이터와 일반 사용자 댓글 구분") {
            val postId = 1L
            val userId = 1L
            val creatorUserId = 2L
            
            val postCommentScenario = PostCommentScenario.create(postId)
            
            postCommentScenario.postComments[0] = postCommentScenario.postComments[0].custom {
                set(PostComment::writerId, creatorUserId).set(PostComment::isCreator, true)
            }
            
            val savedPostComments = postCommentScenario.postComments.map { postCommentRepository.save(it) }
            val savedCreatorComment = savedPostComments[0]
            
            every { postPermissionChecker.hasCommentPermission(userId, postId) } returns true
            every { userQueryAPI.getUsers(any()) } returns postCommentScenario.userQueryDatas
            
            val pagingInformation = PagingInformation.max
            
            When("댓글 목록을 조회하면") {
                val result = postCommentReader.getPostCommentDetails(userId, postId, pagingInformation)
                
                Then("크리에이터와 일반 사용자를 구분할 수 있다") {
                    val creatorComment = result.find { it.id == savedCreatorComment.id }!!
                    creatorComment.isCreator shouldBe true
                }
            }
        }
    }
}