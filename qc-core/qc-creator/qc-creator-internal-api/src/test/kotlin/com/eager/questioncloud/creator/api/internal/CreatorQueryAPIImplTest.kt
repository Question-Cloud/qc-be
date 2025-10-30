package com.eager.questioncloud.creator.api.internal

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.domain.CreatorStatistics
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.test.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class CreatorQueryAPIImplTest(
    private val creatorQueryAPI: CreatorQueryAPIImpl,
    private val creatorRepository: CreatorRepository,
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {

    init {
        afterTest {
            dbCleaner.cleanUp()
        }

        Given("크리에이터와 통계 정보가 존재할 때") {
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

            When("크리에이터 정보를 조회하면") {
                val result = creatorQueryAPI.getCreator(savedCreator.id)

                Then("크리에이터 정보와 통계가 함께 반환된다") {
                    result shouldNotBe null
                    result.userId shouldBe userId
                    result.creatorId shouldBe savedCreator.id
                    result.mainSubject shouldBe mainSubject
                    result.rate shouldBe 4.5
                    result.sales shouldBe 10
                    result.subscriberCount shouldBe 0
                }
            }
        }

        Given("여러 크리에이터가 존재할 때") {
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

            When("여러 크리에이터 정보를 조회하면") {
                val result = creatorQueryAPI.getCreators(creatorIds)

                Then("모든 크리에이터 정보가 반환된다") {
                    result shouldHaveSize 3

                    val creator1Data = result.find { it.creatorId == savedCreator1.id }
                    creator1Data shouldNotBe null
                    creator1Data!!.userId shouldBe userId1
                    creator1Data.mainSubject shouldBe "영어"
                    creator1Data.rate shouldBe 3.0
                    creator1Data.sales shouldBe 5

                    val creator2Data = result.find { it.creatorId == savedCreator2.id }
                    creator2Data shouldNotBe null
                    creator2Data!!.userId shouldBe userId2
                    creator2Data.mainSubject shouldBe "과학"
                    creator2Data.rate shouldBe 4.5
                    creator2Data.sales shouldBe 15

                    val creator3Data = result.find { it.creatorId == savedCreator3.id }
                    creator3Data shouldNotBe null
                    creator3Data!!.userId shouldBe userId3
                    creator3Data.mainSubject shouldBe "역사"
                    creator3Data.rate shouldBe 0.0
                    creator3Data.sales shouldBe 8
                }
            }
        }

        Given("크리에이터가 존재할 때") {
            val userId = 109L
            val creator = Creator.create(userId, "요리", "요리 전문가")
            val savedCreator = creatorRepository.save(creator)

            When("크리에이터 존재 여부를 확인하면") {
                val existsResult = creatorQueryAPI.isExistsById(savedCreator.id)
                val notExistsResult = creatorQueryAPI.isExistsById(999L)

                Then("존재하는 크리에이터는 true, 존재하지 않는 크리에이터는 false가 반환된다") {
                    existsResult shouldBe true
                    notExistsResult shouldBe false
                }
            }
        }
    }
}
