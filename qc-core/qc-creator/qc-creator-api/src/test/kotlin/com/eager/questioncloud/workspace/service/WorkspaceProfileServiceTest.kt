package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.scenario.CreatorScenario
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class WorkspaceProfileServiceTest(
    private val workspaceProfileService: WorkspaceProfileService,
    private val creatorRepository: CreatorRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("크리에이터 프로필 업데이트") {
            val scenario = CreatorScenario.create(1)
            creatorRepository.save(scenario.creators[0])
            
            When("나의 크리에이터 프로필을 업데이트하면") {
                workspaceProfileService.updateCreatorProfile(scenario.creators[0].id, "mainSubject", "introduction")
                
                Then("프로필이 성공적으로 업데이트된다") {
                    val updatedCreator = creatorRepository.findByUserId(scenario.creators[0].userId)!!
                    updatedCreator shouldNotBe null
                    updatedCreator.mainSubject shouldBe "mainSubject"
                    updatedCreator.introduction shouldBe "introduction"
                }
            }
        }
        
        Given("크리에이터 프로필 조회") {
            val scenario = CreatorScenario.create(1)
            creatorRepository.save(scenario.creators[0])
            
            When("나의 크리에이터 프로필 정보를 조회하면") {
                val actualCreator = workspaceProfileService.me(scenario.creators[0].id)
                
                Then("크리에이터 프로필 정보가 반환된다") {
                    actualCreator shouldNotBe null
                    actualCreator.userId shouldBe scenario.creators[0].id
                    actualCreator.mainSubject shouldBe scenario.creators[0].mainSubject
                    actualCreator.introduction shouldBe scenario.creators[0].introduction
                }
            }
        }
    }
}