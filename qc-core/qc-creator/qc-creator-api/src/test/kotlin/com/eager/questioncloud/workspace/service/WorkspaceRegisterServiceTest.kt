package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.user.api.internal.UserCommandAPI
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.justRun
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class WorkspaceRegisterServiceTest(
    private val workspaceRegisterService: WorkspaceRegisterService,
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val dbCleaner: DBCleaner,
) : FunSpec() {
    @MockkBean
    private lateinit var userCommandAPI: UserCommandAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        test("크리에이터를 등록할 수 있다") {
            val userId = 100L
            val mainSubject = "수학"
            val introduction = "안녕하세요, 수학 전문 크리에이터입니다."
            
            justRun { userCommandAPI.toCreator(userId) }
            
            val result = workspaceRegisterService.register(userId, mainSubject, introduction)
            
            result shouldNotBe null
            result.userId shouldBe userId
            result.mainSubject shouldBe mainSubject
            result.introduction shouldBe introduction
        }
        
        test("크리에이터 등록 시 통계 정보가 함께 생성된다") {
            val userId = 101L
            val mainSubject = "영어"
            val introduction = "영어 전문 강사입니다."
            
            justRun { userCommandAPI.toCreator(userId) }
            
            val creator = workspaceRegisterService.register(userId, mainSubject, introduction)
            
            val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creator.id)
            creatorStatistics shouldNotBe null
            creatorStatistics.creatorId shouldBe creator.id
            creatorStatistics.salesCount shouldBe 0
            creatorStatistics.reviewCount shouldBe 0
            creatorStatistics.totalReviewRate shouldBe 0
            creatorStatistics.averageRateOfReview shouldBe 0.0
            creatorStatistics.subscriberCount shouldBe 0
        }
    }
}
