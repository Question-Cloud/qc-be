package com.eager.questioncloud.creator.implement

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.domain.CreatorStatistics
import com.eager.questioncloud.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class CreatorInformationReaderTest(
    @Autowired val creatorInformationReader: CreatorInformationReader,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val creatorStatisticsRepository: CreatorStatisticsRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var userQueryAPI: UserQueryAPI

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `단일 Creator 정보를 조회할 수 있다`() {
        // given
        val creator = Creator(
            userId = 1L,
            mainSubject = "수학",
            introduction = "수학을 가르칩니다."
        )
        val savedCreator = creatorRepository.save(creator)

        val creatorStatistics = CreatorStatistics(
            creatorId = savedCreator.id,
            salesCount = 100,
            averageRateOfReview = 4.5,
            subscriberCount = 50
        )
        creatorStatisticsRepository.save(creatorStatistics)

        val userQueryData = UserQueryData(
            userId = 1L,
            email = "test@example.com",
            name = "테스트 사용자",
            profileImage = "profile.jpg"
        )
        given(userQueryAPI.getUser(any())).willReturn(userQueryData)

        // when
        val result = creatorInformationReader.getCreatorInformation(savedCreator.id)

        // then
        assertThat(result.creatorProfile.creatorId).isEqualTo(savedCreator.id)
        assertThat(result.creatorProfile.name).isEqualTo(userQueryData.name)
        assertThat(result.creatorProfile.profileImage).isEqualTo(userQueryData.profileImage)
        assertThat(result.creatorProfile.mainSubject).isEqualTo(savedCreator.mainSubject)
        assertThat(result.creatorProfile.email).isEqualTo(userQueryData.email)
        assertThat(result.creatorProfile.introduction).isEqualTo(savedCreator.introduction)
        assertThat(result.salesCount).isEqualTo(creatorStatistics.salesCount)
        assertThat(result.averageRateOfReview).isEqualTo(creatorStatistics.averageRateOfReview)
        assertThat(result.subscriberCount).isEqualTo(creatorStatistics.subscriberCount)
    }

    @Test
    fun `여러 Creator 정보를 조회할 수 있다`() {
        // given
        val creator1 = Creator(
            userId = 1L,
            mainSubject = "수학",
            introduction = "수학을 가르칩니다."
        )
        val creator2 = Creator(
            userId = 2L,
            mainSubject = "영어",
            introduction = "영어를 가르칩니다."
        )
        val savedCreator1 = creatorRepository.save(creator1)
        val savedCreator2 = creatorRepository.save(creator2)

        val creatorStatistics1 = CreatorStatistics(
            creatorId = savedCreator1.id,
            salesCount = 100,
            averageRateOfReview = 4.5,
            subscriberCount = 50
        )
        val creatorStatistics2 = CreatorStatistics(
            creatorId = savedCreator2.id,
            salesCount = 200,
            averageRateOfReview = 4.0,
            subscriberCount = 100
        )
        creatorStatisticsRepository.save(creatorStatistics1)
        creatorStatisticsRepository.save(creatorStatistics2)

        val userQueryData1 = UserQueryData(
            userId = 1L,
            email = "test1@example.com",
            name = "테스트 사용자1",
            profileImage = "profile1.jpg"
        )
        val userQueryData2 = UserQueryData(
            userId = 2L,
            email = "test2@example.com",
            name = "테스트 사용자2",
            profileImage = "profile2.jpg"
        )
        given(userQueryAPI.getUsers(any())).willReturn(listOf(userQueryData1, userQueryData2))

        // when
        val results = creatorInformationReader.getCreatorInformation(listOf(savedCreator1.id, savedCreator2.id))

        // then
        assertThat(results).hasSize(2)

        val result1 = results.find { it.creatorProfile.creatorId == savedCreator1.id }
        assertThat(result1).isNotNull
        assertThat(result1!!.creatorProfile.name).isEqualTo(userQueryData1.name)
        assertThat(result1.salesCount).isEqualTo(creatorStatistics1.salesCount)

        val result2 = results.find { it.creatorProfile.creatorId == savedCreator2.id }
        assertThat(result2).isNotNull
        assertThat(result2!!.creatorProfile.name).isEqualTo(userQueryData2.name)
        assertThat(result2.salesCount).isEqualTo(creatorStatistics2.salesCount)
    }
}
