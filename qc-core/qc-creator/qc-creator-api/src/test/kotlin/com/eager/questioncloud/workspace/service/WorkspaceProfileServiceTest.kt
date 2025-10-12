package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.repository.CreatorRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class WorkspaceProfileServiceTest : BehaviorSpec() {
    private val creatorRepository = mockk<CreatorRepository>()

    private val workspaceProfileService = WorkspaceProfileService(
        creatorRepository
    )

    init {
        afterEach {
            clearMocks(creatorRepository)
        }

        Given("크리에이터 프로필 업데이트") {
            val userId = 1L
            val mainSubject = "수학"
            val introduction = "수학 전문 크리에이터입니다."

            val creator = Creator.create(userId, "기존 주제", "기존 소개")

            every { creatorRepository.findByUserId(userId) } returns creator
            every { creatorRepository.save(any()) } returns creator

            When("크리에이터 프로필을 업데이트하면") {
                workspaceProfileService.updateCreatorProfile(userId, mainSubject, introduction)

                Then("프로필이 업데이트된다") {
                    creator.mainSubject shouldBe mainSubject
                    creator.introduction shouldBe introduction

                    verify(exactly = 1) { creatorRepository.findByUserId(userId) }
                    verify(exactly = 1) { creatorRepository.save(creator) }
                }
            }
        }

        Given("존재하지 않는 크리에이터 프로필 업데이트 시도") {
            val userId = 1L
            val mainSubject = "수학"
            val introduction = "수학 전문 크리에이터입니다."

            every { creatorRepository.findByUserId(userId) } returns null

            When("크리에이터 프로필을 업데이트하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        workspaceProfileService.updateCreatorProfile(userId, mainSubject, introduction)
                    }

                    verify(exactly = 1) { creatorRepository.findByUserId(userId) }
                    verify(exactly = 0) { creatorRepository.save(any()) }
                }
            }
        }

        Given("크리에이터 프로필 조회") {
            val userId = 1L

            val creator = Creator.create(userId, "수학", "수학 전문 크리에이터입니다.")

            every { creatorRepository.findByUserId(userId) } returns creator

            When("나의 크리에이터 프로필 정보를 조회하면") {
                val result = workspaceProfileService.me(userId)

                Then("크리에이터 프로필 정보가 반환된다") {
                    result.userId shouldBe creator.userId
                    result.mainSubject shouldBe creator.mainSubject
                    result.introduction shouldBe creator.introduction

                    verify(exactly = 1) { creatorRepository.findByUserId(userId) }
                }
            }
        }

        Given("존재하지 않는 크리에이터 프로필 조회 시도") {
            val userId = 1L

            every { creatorRepository.findByUserId(userId) } returns null

            When("크리에이터 프로필 정보를 조회하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        workspaceProfileService.me(userId)
                    }

                    verify(exactly = 1) { creatorRepository.findByUserId(userId) }
                }
            }
        }
    }
}