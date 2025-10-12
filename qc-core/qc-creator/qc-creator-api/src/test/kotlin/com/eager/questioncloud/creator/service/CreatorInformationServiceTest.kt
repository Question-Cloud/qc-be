package com.eager.questioncloud.creator.service

import com.eager.questioncloud.creator.domain.CreatorProfile
import com.eager.questioncloud.creator.dto.CreatorInformation
import com.eager.questioncloud.creator.implement.CreatorInformationReader
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CreatorInformationServiceTest : BehaviorSpec() {
    private val creatorInformationReader = mockk<CreatorInformationReader>()

    private val creatorInformationService = CreatorInformationService(
        creatorInformationReader
    )

    init {
        afterEach {
            clearMocks(creatorInformationReader)
        }

        Given("크리에이터 정보 조회") {
            val creatorId = 1L

            val creatorProfile = CreatorProfile(
                creatorId = creatorId,
                name = "크리에이터",
                profileImage = "profile.jpg",
                mainSubject = "수학",
                email = "creator@example.com",
                introduction = "수학 전문 크리에이터입니다."
            )

            val creatorInformation = CreatorInformation(
                creatorProfile = creatorProfile,
                salesCount = 100,
                averageRateOfReview = 4.5,
                subscriberCount = 500
            )

            every { creatorInformationReader.getCreatorInformation(creatorId) } returns creatorInformation

            When("크리에이터 정보를 조회하면") {
                val result = creatorInformationService.getCreatorInformation(creatorId)

                Then("크리에이터 정보가 반환된다") {
                    result.creatorProfile.creatorId shouldBe creatorId
                    result.creatorProfile.name shouldBe "크리에이터"
                    result.salesCount shouldBe 100
                    result.averageRateOfReview shouldBe 4.5
                    result.subscriberCount shouldBe 500

                    verify(exactly = 1) { creatorInformationReader.getCreatorInformation(creatorId) }
                }
            }
        }
    }
}