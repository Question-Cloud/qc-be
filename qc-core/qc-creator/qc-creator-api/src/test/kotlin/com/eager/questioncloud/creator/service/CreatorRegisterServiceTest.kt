package com.eager.questioncloud.creator.service

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.implement.CreatorRegister
import com.eager.questioncloud.creator.implement.CreatorStatisticsInitializer
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class CreatorRegisterServiceTest : BehaviorSpec() {
    private val creatorRegister = mockk<CreatorRegister>()
    private val creatorStatisticsInitializer = mockk<CreatorStatisticsInitializer>()
    
    private val registerCreatorService = RegisterCreatorService(
        creatorRegister,
        creatorStatisticsInitializer
    )
    
    init {
        afterEach {
            clearMocks(creatorRegister, creatorStatisticsInitializer)
        }
        
        Given("크리에이터 등록") {
            val userId = 1L
            val mainSubject = "수학"
            val introduction = "안녕하세요, 수학 전문 크리에이터입니다."
            
            val creator = Creator.create(userId, mainSubject, introduction)
            
            every { creatorRegister.register(userId, mainSubject, introduction) } returns creator
            justRun { creatorStatisticsInitializer.initializeCreatorStatistics(any()) }
            
            When("크리에이터를 등록하면") {
                val result = registerCreatorService.register(userId, mainSubject, introduction)
                
                Then("크리에이터가 성공적으로 등록된다") {
                    result.userId shouldBe creator.userId
                    result.mainSubject shouldBe creator.mainSubject
                    result.introduction shouldBe creator.introduction
                    
                    verify(exactly = 1) { creatorRegister.register(userId, mainSubject, introduction) }
                    verify(exactly = 1) { creatorStatisticsInitializer.initializeCreatorStatistics(any()) }
                }
            }
        }
    }
}