package com.eager.questioncloud.workspace.implement

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
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class WorkspacePostReaderTest(
    private val workspacePostReader: WorkspacePostReader,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
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
        
        Given("워크스페이스에서 크리에이터 게시글 조회") {
            When("크리에이터 문제와 관련된 게시글을 조회하면") {
                val creatorId = 1L
                
                val writer1Id = 1L
                val writer2Id = 2L
                
                val question1Id = 1L
                val question2Id = 2L
                
                val post1Id = 1L
                val post2Id = 2L
                
                val pagingInformation = PagingInformation.max
                
                val question1QueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                    .set(QuestionInformationQueryResult::id, question1Id)
                    .set(QuestionInformationQueryResult::creatorId, creatorId)
                    .sample()
                
                val question2QueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                    .set(QuestionInformationQueryResult::id, question2Id)
                    .set(QuestionInformationQueryResult::creatorId, creatorId)
                    .sample()
                
                every { questionQueryAPI.getCreatorQuestions(any(), any()) } returns listOf(question1QueryData, question2QueryData)
                
                val post1QueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorPostQueryAPIResult>()
                    .set(CreatorPostQueryAPIResult::id, post1Id)
                    .set(CreatorPostQueryAPIResult::writerId, writer1Id)
                    .sample()
                
                val post2QueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorPostQueryAPIResult>()
                    .set(CreatorPostQueryAPIResult::id, post2Id)
                    .set(CreatorPostQueryAPIResult::writerId, writer2Id)
                    .sample()
                
                every { postQueryAPI.getCreatorPosts(any(), any()) } returns listOf(post1QueryData, post2QueryData)
                
                val user1QueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>()
                    .set(UserQueryData::userId, 1)
                    .sample()
                
                val user2QueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>()
                    .set(UserQueryData::userId, 2)
                    .sample()
                
                every { userQueryAPI.getUsers(any()) } returns listOf(user1QueryData, user2QueryData)
                
                val result = workspacePostReader.getCreatorPosts(creatorId, pagingInformation)
                
                Then("게시글 목록이 반환된다") {
                    result shouldHaveSize 2
                    result[0].id shouldBe post1Id
                    result[0].title shouldBe post1QueryData.title
                    result[0].writer shouldBe user1QueryData.name
                    result[1].id shouldBe post2Id
                    result[1].title shouldBe post2QueryData.title
                    result[1].writer shouldBe user2QueryData.name
                }
            }
            
            When("크리에이터의 게시글 개수를 조회하면") {
                val creatorId = 1L
                val question = Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                    .set(QuestionInformationQueryResult::id, 1L)
                    .sample()
                
                every { questionQueryAPI.getCreatorQuestions(creatorId, PagingInformation.max) } returns listOf(question)
                every { postQueryAPI.countByQuestionIdIn(any()) } returns 5
                
                val result = workspacePostReader.countCreatorPost(creatorId)
                
                Then("게시글 개수가 반환된다") {
                    result shouldBe 5
                }
            }
        }
    }
}