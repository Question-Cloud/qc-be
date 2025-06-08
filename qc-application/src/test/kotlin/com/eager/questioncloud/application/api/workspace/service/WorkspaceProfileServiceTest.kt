package com.eager.questioncloud.application.api.workspace.service

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.question.enums.Subject
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class WorkspaceProfileServiceTest(
    @Autowired val workspaceProfileService: WorkspaceProfileService,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val userRepository: UserRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터의 프로필을 업데이트할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "creator@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val creator = CreatorFixtureHelper.createCreator(user.uid, creatorRepository)

        val newMainSubject = Subject.Mathematics
        val newIntroduction = "안녕하세요! 수학 전문 크리에이터로 활동하고 있습니다."

        // when
        workspaceProfileService.updateCreatorProfile(creator, newMainSubject, newIntroduction)

        // then
        val updatedCreator = creatorRepository.findById(creator.id)

        Assertions.assertThat(updatedCreator.mainSubject).isEqualTo(newMainSubject)
        Assertions.assertThat(updatedCreator.introduction).isEqualTo(newIntroduction)
    }
}
