package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.api.internal.PostQueryAPI
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import com.eager.questioncloud.workspace.scenario.WorkspacePostScenario
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
            val creatorId = 1L
            val pagingInformation = PagingInformation.max
            val scenario = WorkspacePostScenario.create(10)
            
            every { questionQueryAPI.getCreatorQuestions(any(), any()) } returns scenario.questionInformationQueryResults
            every { postQueryAPI.getCreatorPosts(any(), any()) } returns scenario.creatorPostQueryAPIResults
            every { userQueryAPI.getUsers(any()) } returns scenario.userQueryDatas
            
            When("크리에이터 문제와 관련된 게시글을 조회하면") {
                val result = workspacePostReader.getCreatorPosts(creatorId, pagingInformation)
                
                Then("게시글 목록이 반환된다") {
                    result shouldHaveSize scenario.creatorPostQueryAPIResults.size
                    
                    result.forEach { item ->
                        val postId = item.id
                        val postInfo = scenario.creatorPostQueryAPIResults.find { it.id == postId }!!
                        val writerInfo = scenario.userQueryDatas.find { it.userId == postInfo.writerId }!!
                        
                        item.title shouldBe postInfo.title
                        item.writer shouldBe writerInfo.name
                    }
                }
            }
        }
        
        Given("워크스페이스에서 크리에이터 게시글 페이징 정보 조회") {
            val creatorId = 1L
            val scenario = WorkspacePostScenario.create(10)
            
            every {
                questionQueryAPI.getCreatorQuestions(
                    creatorId,
                    PagingInformation.max
                )
            } returns scenario.questionInformationQueryResults
            
            every { postQueryAPI.countByQuestionIdIn(any()) } returns scenario.creatorPostQueryAPIResults.size
            
            When("크리에이터의 게시글 개수를 조회하면") {
                val result = workspacePostReader.countCreatorPost(creatorId)
                
                Then("게시글 개수가 반환된다") {
                    result shouldBe scenario.creatorPostQueryAPIResults.size
                }
            }
        }
    }
}