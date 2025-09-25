package com.eager.questioncloud.creator.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.scenario.CreatorScenario
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.utils.DBCleaner
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
            val creatorScenario = CreatorScenario.create(1)
            creatorRepository.save(creatorScenario.creators[0])
            creatorStatisticsRepository.save(creatorScenario.creatorStatisticses[0])
            every { userQueryAPI.getUser(any()) } returns creatorScenario.creatorUserQueryDatas[0]
            
            When("유효한 CreatorId라면") {
                val result = creatorInformationReader.getCreatorInformation(creatorScenario.creators[0].id)
                
                Then("크리에이터의 정보가 조회된다.") {
                    result.creatorProfile.creatorId shouldBe creatorScenario.creators[0].id
                    result.creatorProfile.name shouldBe creatorScenario.creatorUserQueryDatas[0].name
                    result.creatorProfile.profileImage shouldBe creatorScenario.creatorUserQueryDatas[0].profileImage
                    result.creatorProfile.mainSubject shouldBe creatorScenario.creators[0].mainSubject
                    result.creatorProfile.email shouldBe creatorScenario.creatorUserQueryDatas[0].email
                    result.creatorProfile.introduction shouldBe creatorScenario.creators[0].introduction
                    result.salesCount shouldBe creatorScenario.creatorStatisticses[0].salesCount
                    result.averageRateOfReview shouldBe creatorScenario.creatorStatisticses[0].averageRateOfReview
                    result.subscriberCount shouldBe creatorScenario.creatorStatisticses[0].subscriberCount
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
