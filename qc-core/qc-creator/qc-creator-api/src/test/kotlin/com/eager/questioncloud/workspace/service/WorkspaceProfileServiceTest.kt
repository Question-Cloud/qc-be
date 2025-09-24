package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.repository.CreatorRepository
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
        
        Given("크리에이터 프로필 관리") {
            When("크리에이터 프로필을 업데이트하면") {
                val userId = 1L
                val mainSubject = "수학"
                val introduction = "안녕하세요, 수학 강사입니다."
                val creator = Creator(userId = userId, mainSubject = "기존 과목", introduction = "기존 소개")
                creatorRepository.save(creator)
                
                workspaceProfileService.updateCreatorProfile(userId, mainSubject, introduction)
                
                Then("프로필이 성공적으로 업데이트된다") {
                    val updatedCreator = creatorRepository.findByUserId(userId)
                    updatedCreator shouldNotBe null
                    updatedCreator?.mainSubject shouldBe mainSubject
                    updatedCreator?.introduction shouldBe introduction
                }
            }
            
            When("나의 크리에이터 프로필 정보를 조회하면") {
                val userId = 1L
                val expectedCreator = Creator(userId = userId, mainSubject = "수학", introduction = "안녕하세요")
                creatorRepository.save(expectedCreator)
                
                val actualCreator = workspaceProfileService.me(userId)
                
                Then("크리에이터 프로필 정보가 반환된다") {
                    actualCreator shouldNotBe null
                    actualCreator.userId shouldBe expectedCreator.userId
                    actualCreator.mainSubject shouldBe expectedCreator.mainSubject
                    actualCreator.introduction shouldBe expectedCreator.introduction
                }
            }
        }
    }
}