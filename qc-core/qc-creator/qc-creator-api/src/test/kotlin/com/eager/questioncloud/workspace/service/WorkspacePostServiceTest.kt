package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.workspace.dto.CreatorPostItem
import com.eager.questioncloud.workspace.implement.WorkspacePostReader
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

class WorkspacePostServiceTest : BehaviorSpec() {
    private val workspacePostReader = mockk<WorkspacePostReader>()
    
    private val workspacePostService = WorkspacePostService(
        workspacePostReader
    )
    
    init {
        afterEach {
            clearMocks(workspacePostReader)
        }
        
        Given("크리에이터의 게시글 목록 조회") {
            val creatorId = 1L
            val pagingInformation = PagingInformation(0, 10)
            
            val creatorPostItems = listOf(
                CreatorPostItem(
                    id = 1L,
                    title = "첫 번째 게시글",
                    writer = "작성자1",
                    createdAt = LocalDateTime.now()
                ),
                CreatorPostItem(
                    id = 2L,
                    title = "두 번째 게시글",
                    writer = "작성자2",
                    createdAt = LocalDateTime.now()
                )
            )
            
            every { workspacePostReader.getCreatorPosts(creatorId, pagingInformation) } returns creatorPostItems
            
            When("크리에이터의 게시글 목록을 조회하면") {
                val result = workspacePostService.getCreatorPosts(creatorId, pagingInformation)

                Then("게시글 목록이 반환된다") {
                    result shouldHaveSize 2
                    result[0].title shouldBe creatorPostItems[0].title
                    result[0].writer shouldBe creatorPostItems[0].writer
                    result[1].title shouldBe creatorPostItems[1].title
                    result[1].writer shouldBe creatorPostItems[1].writer

                    verify(exactly = 1) { workspacePostReader.getCreatorPosts(creatorId, pagingInformation) }
                }
            }
        }
        
        Given("크리에이터의 게시글 개수 조회") {
            val creatorId = 1L
            
            every { workspacePostReader.countCreatorPost(creatorId) } returns 5
            
            When("크리에이터의 게시글 개수를 조회하면") {
                val result = workspacePostService.countCreatorPost(creatorId)
                
                Then("게시글 개수가 반환된다") {
                    result shouldBe 5
                    
                    verify(exactly = 1) { workspacePostReader.countCreatorPost(creatorId) }
                }
            }
        }
    }
}