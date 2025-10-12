package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.workspace.implement.CreatorStatisticsInitializer
import com.eager.questioncloud.workspace.implement.WorkspaceCreatorRegister
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

class WorkspaceRegisterServiceTest : BehaviorSpec() {
    private val workspaceCreatorRegister = mockk<WorkspaceCreatorRegister>()
    private val creatorStatisticsInitializer = mockk<CreatorStatisticsInitializer>()

    private val workspaceRegisterService = WorkspaceRegisterService(
        workspaceCreatorRegister,
        creatorStatisticsInitializer
    )

    init {
        afterEach {
            clearMocks(workspaceCreatorRegister, creatorStatisticsInitializer)
        }

        Given("크리에이터 등록") {
            val userId = 1L
            val mainSubject = "수학"
            val introduction = "안녕하세요, 수학 전문 크리에이터입니다."

            val creator = Creator.create(userId, mainSubject, introduction)

            every { workspaceCreatorRegister.register(userId, mainSubject, introduction) } returns creator
            justRun { creatorStatisticsInitializer.initializeCreatorStatistics(any()) }

            When("크리에이터를 등록하면") {
                val result = workspaceRegisterService.register(userId, mainSubject, introduction)

                Then("크리에이터가 성공적으로 등록된다") {
                    result.userId shouldBe creator.userId
                    result.mainSubject shouldBe creator.mainSubject
                    result.introduction shouldBe creator.introduction

                    verify(exactly = 1) { workspaceCreatorRegister.register(userId, mainSubject, introduction) }
                    verify(exactly = 1) { creatorStatisticsInitializer.initializeCreatorStatistics(any()) }
                }
            }
        }
    }
}