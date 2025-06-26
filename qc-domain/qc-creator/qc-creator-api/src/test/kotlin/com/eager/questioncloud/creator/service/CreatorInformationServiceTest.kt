package com.eager.questioncloud.creator.service

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.domain.CreatorStatistics
import com.eager.questioncloud.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class CreatorInformationServiceTest(
    @Autowired val creatorInformationService: CreatorInformationService,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val creatorStatisticsRepository: CreatorStatisticsRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var userQueryAPI: UserQueryAPI

    private val creatorId = 1L
    private val userId = 100L

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터 정보를 조회할 수 있다`() {
        //given
        val creator = Creator(
            id = creatorId,
            userId = userId,
            mainSubject = "수학",
            introduction = "안녕하세요. 테스트 크리에이터입니다."
        )
        creatorRepository.save(creator)

        val creatorStatistics = CreatorStatistics(
            creatorId = creatorId,
            salesCount = 100,
            averageRateOfReview = 4.5,
            subscriberCount = 50
        )
        creatorStatisticsRepository.save(creatorStatistics)

        val userQueryData = UserQueryData(
            userId = userId,
            name = "테스트 유저",
            profileImage = "profile.jpg",
            email = "test@test.com"
        )

        given(userQueryAPI.getUser(userId)).willReturn(userQueryData)

        //when
        val result = creatorInformationService.getCreatorInformation(creatorId)

        //then
        Assertions.assertThat(result.creatorProfile.name).isEqualTo("테스트 유저")
        Assertions.assertThat(result.salesCount).isEqualTo(100)
        Assertions.assertThat(result.averageRateOfReview).isEqualTo(4.5)
        Assertions.assertThat(result.subscriberCount).isEqualTo(50)
    }
}