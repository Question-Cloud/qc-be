package com.eager.questioncloud.application.api.workspace.service

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.core.domain.question.enums.Subject
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.enums.UserType
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class WorkspaceRegisterServiceTest(
    @Autowired val workspaceRegisterService: WorkspaceRegisterService,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val creatorStatisticsRepository: CreatorStatisticsRepository,
    @Autowired val userRepository: UserRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터로 등록할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "creator@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val mainSubject = Subject.Mathematics
        val introduction = "안녕하세요! 수학 전문 크리에이터입니다."

        // when
        val creator = workspaceRegisterService.register(user, mainSubject, introduction)

        // then

        val savedCreator = creatorRepository.findById(creator.id)
        Assertions.assertThat(savedCreator).isNotNull()
        Assertions.assertThat(savedCreator.userId).isEqualTo(user.uid)
        Assertions.assertThat(savedCreator.mainSubject).isEqualTo(mainSubject)
        Assertions.assertThat(savedCreator.introduction).isEqualTo(introduction)

        val updatedUser = userRepository.getUser(user.uid)
        Assertions.assertThat(updatedUser.userType).isEqualTo(UserType.CreatorUser)

        val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creator.id)
        Assertions.assertThat(creatorStatistics).isNotNull()
        Assertions.assertThat(creatorStatistics.creatorId).isEqualTo(creator.id)
        Assertions.assertThat(creatorStatistics.salesCount).isEqualTo(0)
        Assertions.assertThat(creatorStatistics.reviewCount).isEqualTo(0)
        Assertions.assertThat(creatorStatistics.totalReviewRate).isEqualTo(0)
        Assertions.assertThat(creatorStatistics.averageRateOfReview).isEqualTo(0.0)
    }
}
