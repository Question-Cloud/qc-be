package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.creator.api.internal.CreatorCommandAPI
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.creator.api.internal.CreatorQueryData
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class WorkspaceProfileServiceTest : BehaviorSpec() {
    private val creatorQueryAPI = mockk<CreatorQueryAPI>()
    private val creatorCommandAPI = mockk<CreatorCommandAPI>()
    private val workspaceProfileService = WorkspaceProfileService(creatorQueryAPI, creatorCommandAPI)
    
    init {
        afterEach {
            clearMocks(creatorQueryAPI, creatorCommandAPI)
        }
        
        Given("크리에이터 프로필 업데이트") {
            val creatorId = 1L
            val mainSubject = "수학"
            val introduction = "수학 전문 크리에이터입니다."
            
            justRun { creatorCommandAPI.updateCreatorProfile(creatorId, mainSubject, introduction) }
            
            When("크리에이터 프로필을 업데이트하면") {
                workspaceProfileService.updateCreatorProfile(creatorId, mainSubject, introduction)
                
                Then("프로필이 업데이트된다") {
                    verify(exactly = 1) { creatorCommandAPI.updateCreatorProfile(creatorId, mainSubject, introduction) }
                }
            }
        }
        
        Given("크리에이터 프로필 조회") {
            val userId = 1L
            val creatorId = 1L
            
            val mainSubject = "수학"
            val introduction = "수학 전문 크리에이터입니다."
            
            val creatorQueryData = CreatorQueryData(userId, creatorId, mainSubject, introduction, 4.1, 100, 100)
            
            every { creatorQueryAPI.getCreator(creatorId) } returns creatorQueryData
            
            When("나의 크리에이터 프로필 정보를 조회하면") {
                val result = workspaceProfileService.getProfile(creatorId)
                
                Then("크리에이터 프로필 정보가 반환된다") {
                    result.mainSubject shouldBe creatorQueryData.mainSubject
                    result.introduction shouldBe creatorQueryData.introduction
                    
                    verify(exactly = 1) { creatorQueryAPI.getCreator(userId) }
                }
            }
        }
    }
}