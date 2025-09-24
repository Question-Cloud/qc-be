package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
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
) : FunSpec() {
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        test("크리에이터 프로필을 업데이트할 수 있다") {
            val userId = 1L
            val mainSubject = "수학"
            val introduction = "안녕하세요, 수학 강사입니다."
            val creator = Creator(userId = userId, mainSubject = "기존 과목", introduction = "기존 소개")
            creatorRepository.save(creator)
            
            workspaceProfileService.updateCreatorProfile(userId, mainSubject, introduction)
            
            val updatedCreator = creatorRepository.findByUserId(userId)
            updatedCreator shouldNotBe null
            updatedCreator?.mainSubject shouldBe mainSubject
            updatedCreator?.introduction shouldBe introduction
        }
        
        test("크리에이터일 경우 나의 크리에이터 프로필 정보를 반환한다") {
            val userId = 1L
            val expectedCreator = Creator(userId = userId, mainSubject = "수학", introduction = "안녕하세요")
            creatorRepository.save(expectedCreator)
            
            val actualCreator = workspaceProfileService.me(userId)
            
            actualCreator shouldNotBe null
            actualCreator.userId shouldBe expectedCreator.userId
            actualCreator.mainSubject shouldBe expectedCreator.mainSubject
            actualCreator.introduction shouldBe expectedCreator.introduction
        }
    }
}
