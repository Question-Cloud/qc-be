package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.api.CreatorPostQueryAPIResult
import com.eager.questioncloud.post.api.internal.PostQueryAPI
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.DBCleaner
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class WorkspacePostServiceTest(
    private val workspacePostService: WorkspacePostService,
    private val dbCleaner: DBCleaner,
) : FunSpec() {
    
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    @MockkBean
    private lateinit var postQueryAPI: PostQueryAPI
    
    @MockkBean
    private lateinit var userQueryAPI: UserQueryAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        test("크리에이터가 만든 문제들의 게시글을 조회할 수 있다") {
            val creatorId = 1L
            val writerId = 1L
            val questionId = 1L
            val postId = 1L
            val pagingInformation = PagingInformation(1, 10)
            
            val questionQueryResult = Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                .set(QuestionInformationQueryResult::id, questionId)
                .sample()
            
            val creatorPostQueryAPIResult = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorPostQueryAPIResult>()
                .set(CreatorPostQueryAPIResult::id, postId)
                .set(CreatorPostQueryAPIResult::writerId, writerId)
                .sample()
            
            val userQueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>()
                .set(UserQueryData::userId, writerId)
                .sample()
            
            every { questionQueryAPI.getCreatorQuestions(creatorId, pagingInformation) } returns listOf(questionQueryResult)
            every { postQueryAPI.getCreatorPosts(any(), any()) } returns listOf(creatorPostQueryAPIResult)
            every { userQueryAPI.getUsers(any()) } returns listOf(userQueryData)
            
            val actualPosts = workspacePostService.getCreatorPosts(creatorId, pagingInformation)
            
            actualPosts shouldHaveSize 1
            actualPosts[0].id shouldBe postId
            actualPosts[0].title shouldBe creatorPostQueryAPIResult.title
            actualPosts[0].writer shouldBe userQueryData.name
        }
        
        test("크리에이터가 만든 문제들의 게시글 개수를 조회할 수 있다") {
            val creatorId = 1L
            val questionId = 1L
            val expectedCount = 10
            
            val questionQueryResult = Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                .set(QuestionInformationQueryResult::id, questionId)
                .sample()
            
            every { questionQueryAPI.getCreatorQuestions(creatorId) } returns
                    listOf(questionQueryResult)
            every { postQueryAPI.countByQuestionIdIn(any()) } returns expectedCount
            
            val actualCount = workspacePostService.countCreatorPost(creatorId)
            
            actualCount shouldBe expectedCount
        }
    }
}