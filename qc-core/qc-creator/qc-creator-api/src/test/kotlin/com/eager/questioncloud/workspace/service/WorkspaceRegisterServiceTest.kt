package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.user.api.internal.UserCommandAPI
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doNothing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class WorkspaceRegisterServiceTest(
    @Autowired val workspaceRegisterService: WorkspaceRegisterService,
    @Autowired val creatorStatisticsRepository: CreatorStatisticsRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var userCommandAPI: UserCommandAPI
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `크리에이터를 등록할 수 있다`() {
        //given
        val userId = 100L
        val mainSubject = "수학"
        val introduction = "안녕하세요, 수학 전문 크리에이터입니다."
        
        doNothing().`when`(userCommandAPI).toCreator(userId)
        
        //when
        val result = workspaceRegisterService.register(userId, mainSubject, introduction)
        
        //then
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.userId).isEqualTo(userId)
        Assertions.assertThat(result.mainSubject).isEqualTo(mainSubject)
        Assertions.assertThat(result.introduction).isEqualTo(introduction)
        Assertions.assertThat(result.id).isGreaterThan(0)
    }
    
    @Test
    fun `크리에이터 등록 시 통계 정보가 함께 생성된다`() {
        //given
        val userId = 101L
        val mainSubject = "영어"
        val introduction = "영어 전문 강사입니다."
        
        doNothing().`when`(userCommandAPI).toCreator(userId)
        
        //when
        val creator = workspaceRegisterService.register(userId, mainSubject, introduction)
        
        //then
        val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creator.id)
        Assertions.assertThat(creatorStatistics).isNotNull
        Assertions.assertThat(creatorStatistics.creatorId).isEqualTo(creator.id)
        Assertions.assertThat(creatorStatistics.salesCount).isEqualTo(0)
        Assertions.assertThat(creatorStatistics.reviewCount).isEqualTo(0)
        Assertions.assertThat(creatorStatistics.totalReviewRate).isEqualTo(0)
        Assertions.assertThat(creatorStatistics.averageRateOfReview).isEqualTo(0.0)
        Assertions.assertThat(creatorStatistics.subscriberCount).isEqualTo(0)
    }
}
