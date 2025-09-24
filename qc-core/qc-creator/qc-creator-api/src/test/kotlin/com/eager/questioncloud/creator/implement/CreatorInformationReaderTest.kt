package com.eager.questioncloud.creator.implement

import com.eager.questioncloud.common.exception.CoreException
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
import io.kotest.assertions.throwables.shouldThrow
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
class CreatorInformationReaderTest(
    private val creatorInformationReader: CreatorInformationReader,
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
            When("유효한 CreatorId라면") {
                val creatorId = 1L
                val userId = 1L
                val savedCreator = creatorRepository.save(
                    Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                        .set(Creator::id, creatorId)
                        .set(Creator::userId, userId)
                        .sample()
                )
                
                val creatorStatistics = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorStatistics>()
                    .set(CreatorStatistics::creatorId, creatorId)
                    .sample()
                creatorStatisticsRepository.save(creatorStatistics)
                
                val userQueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>()
                    .set(UserQueryData::userId, userId)
                    .sample()
                
                every { userQueryAPI.getUser(any()) } returns userQueryData
                
                val result = creatorInformationReader.getCreatorInformation(savedCreator.id)
                
                Then("크리에이터의 정보가 조회된다.") {
                    result.creatorProfile.creatorId shouldBe savedCreator.id
                    result.creatorProfile.name shouldBe userQueryData.name
                    result.creatorProfile.profileImage shouldBe userQueryData.profileImage
                    result.creatorProfile.mainSubject shouldBe savedCreator.mainSubject
                    result.creatorProfile.email shouldBe userQueryData.email
                    result.creatorProfile.introduction shouldBe savedCreator.introduction
                    result.salesCount shouldBe creatorStatistics.salesCount
                    result.averageRateOfReview shouldBe creatorStatistics.averageRateOfReview
                    result.subscriberCount shouldBe creatorStatistics.subscriberCount
                }
            }
            
            When("유효하지 않은 CreatorId라면") {
                val unknownId = 1000L
                
                Then("Error.NOT_FOUND 예외가 발생한다.") {
                    shouldThrow<CoreException> {
                        creatorInformationReader.getCreatorInformation(unknownId)
                    }
                }
            }
        }
    }
}
