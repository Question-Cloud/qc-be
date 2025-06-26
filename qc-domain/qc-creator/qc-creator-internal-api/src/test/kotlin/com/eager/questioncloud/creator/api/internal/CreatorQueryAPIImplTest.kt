package com.eager.questioncloud.creator.api.internal

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.domain.CreatorStatistics
import com.eager.questioncloud.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class CreatorQueryAPIImplTest(
    @Autowired val creatorQueryAPI: CreatorQueryAPIImpl,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val creatorStatisticsRepository: CreatorStatisticsRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터 정보를 조회할 수 있다`() {
        //given
        val userId = 100L
        val mainSubject = "수학"
        val introduction = "수학 전문가입니다"

        val creator = Creator.create(userId, mainSubject, introduction)
        val savedCreator = creatorRepository.save(creator)

        val statistics = CreatorStatistics.create(savedCreator.id)
        statistics.addSaleCount(10)
        statistics.updateReviewStatisticsByRegisteredReview(5)
        statistics.updateReviewStatisticsByRegisteredReview(4)
        creatorStatisticsRepository.save(statistics)

        //when
        val result = creatorQueryAPI.getCreator(savedCreator.id)

        //then
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.userId).isEqualTo(userId)
        Assertions.assertThat(result.creatorId).isEqualTo(savedCreator.id)
        Assertions.assertThat(result.mainSubject).isEqualTo(mainSubject)
        Assertions.assertThat(result.rate).isEqualTo(4.5)
        Assertions.assertThat(result.sales).isEqualTo(10)
        Assertions.assertThat(result.subscriberCount).isEqualTo(0)
    }

    @Test
    fun `여러 크리에이터 정보를 조회할 수 있다`() {
        //given
        val userId1 = 101L
        val userId2 = 102L
        val userId3 = 103L

        val creator1 = Creator.create(userId1, "영어", "영어 전문가")
        val creator2 = Creator.create(userId2, "과학", "과학 전문가")
        val creator3 = Creator.create(userId3, "역사", "역사 전문가")
        val savedCreator1 = creatorRepository.save(creator1)
        val savedCreator2 = creatorRepository.save(creator2)
        val savedCreator3 = creatorRepository.save(creator3)

        val statistics1 = CreatorStatistics.create(savedCreator1.id)
        statistics1.addSaleCount(5)
        statistics1.updateReviewStatisticsByRegisteredReview(3)

        val statistics2 = CreatorStatistics.create(savedCreator2.id)
        statistics2.addSaleCount(15)
        statistics2.updateReviewStatisticsByRegisteredReview(5)
        statistics2.updateReviewStatisticsByRegisteredReview(4)

        val statistics3 = CreatorStatistics.create(savedCreator3.id)
        statistics3.addSaleCount(8)

        creatorStatisticsRepository.save(statistics1)
        creatorStatisticsRepository.save(statistics2)
        creatorStatisticsRepository.save(statistics3)

        val creatorIds = listOf(savedCreator1.id, savedCreator2.id, savedCreator3.id)

        //when
        val result = creatorQueryAPI.getCreators(creatorIds)

        //then
        Assertions.assertThat(result).hasSize(3)

        val creator1Data = result.find { it.creatorId == savedCreator1.id }
        Assertions.assertThat(creator1Data).isNotNull
        Assertions.assertThat(creator1Data!!.userId).isEqualTo(userId1)
        Assertions.assertThat(creator1Data.mainSubject).isEqualTo("영어")
        Assertions.assertThat(creator1Data.rate).isEqualTo(3.0)
        Assertions.assertThat(creator1Data.sales).isEqualTo(5)

        val creator2Data = result.find { it.creatorId == savedCreator2.id }
        Assertions.assertThat(creator2Data).isNotNull
        Assertions.assertThat(creator2Data!!.userId).isEqualTo(userId2)
        Assertions.assertThat(creator2Data.mainSubject).isEqualTo("과학")
        Assertions.assertThat(creator2Data.rate).isEqualTo(4.5)
        Assertions.assertThat(creator2Data.sales).isEqualTo(15)

        val creator3Data = result.find { it.creatorId == savedCreator3.id }
        Assertions.assertThat(creator3Data).isNotNull
        Assertions.assertThat(creator3Data!!.userId).isEqualTo(userId3)
        Assertions.assertThat(creator3Data.mainSubject).isEqualTo("역사")
        Assertions.assertThat(creator3Data.rate).isEqualTo(0.0)
        Assertions.assertThat(creator3Data.sales).isEqualTo(8)
    }

    @Test
    fun `크리에이터 존재 여부를 확인할 수 있다`() {
        //given
        val userId = 109L
        val creator = Creator.create(userId, "요리", "요리 전문가")
        val savedCreator = creatorRepository.save(creator)

        //when
        val existsResult = creatorQueryAPI.isExistsById(savedCreator.id)
        val notExistsResult = creatorQueryAPI.isExistsById(999L)

        //then
        Assertions.assertThat(existsResult).isTrue()
        Assertions.assertThat(notExistsResult).isFalse()
    }
}
