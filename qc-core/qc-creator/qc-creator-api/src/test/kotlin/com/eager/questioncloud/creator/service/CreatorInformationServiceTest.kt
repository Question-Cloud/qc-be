package com.eager.questioncloud.creator.service

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.domain.CreatorStatistics
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.DBCleaner
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class CreatorInformationServiceTest(
    private val creatorInformationService: CreatorInformationService,
    private val creatorRepository: CreatorRepository,
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    
    @MockkBean
    private lateinit var userQueryAPI: UserQueryAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("크리에이터 정보 조회") {
            When("크리에이터 정보를 조회하면") {
                val creatorId = 1L
                val userId = 1L
                
                val creator = Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                    .set(Creator::id, creatorId)
                    .set(Creator::userId, userId)
                    .sample()
                
                creatorRepository.save(creator)
                
                val creatorStatistics = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorStatistics>()
                    .set(CreatorStatistics::creatorId, creatorId)
                    .sample()
                
                creatorStatisticsRepository.save(creatorStatistics)
                
                val userQueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>()
                    .set(UserQueryData::userId, userId)
                    .sample()
                
                every { userQueryAPI.getUser(userId) } returns userQueryData
                
                val result = creatorInformationService.getCreatorInformation(creatorId)
                
                Then("크리에이터의 모든 정보가 반환된다") {
                    result.creatorProfile.name shouldBe userQueryData.name
                    result.salesCount shouldBe creatorStatistics.salesCount
                    result.averageRateOfReview shouldBe creatorStatistics.averageRateOfReview
                    result.subscriberCount shouldBe creatorStatistics.subscriberCount
                }
            }
        }
    }
}