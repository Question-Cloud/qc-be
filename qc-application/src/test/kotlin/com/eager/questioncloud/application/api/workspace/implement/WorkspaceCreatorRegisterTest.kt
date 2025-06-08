package com.eager.questioncloud.application.api.workspace.implement

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
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
class WorkspaceCreatorRegisterTest(
    @Autowired val workspaceCreatorRegister: WorkspaceCreatorRegister,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val userRepository: UserRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `일반 사용자를 크리에이터로 등록할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "creator@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )

        val mainSubject = Subject.Mathematics
        val introduction = "안녕하세요. 수학 전문 입니다."

        // when
        val creator = workspaceCreatorRegister.register(user, mainSubject, introduction)

        // then
        Assertions.assertThat(creator.userId).isEqualTo(user.uid)
        Assertions.assertThat(creator.mainSubject).isEqualTo(mainSubject)
        Assertions.assertThat(creator.introduction).isEqualTo(introduction)
        Assertions.assertThat(creator.id).isGreaterThan(0)

        val updatedUser = userRepository.getUser(user.uid)
        Assertions.assertThat(updatedUser.userType).isEqualTo(UserType.CreatorUser)

        val savedCreator = creatorRepository.findById(creator.id)
        Assertions.assertThat(savedCreator).isNotNull()
        Assertions.assertThat(savedCreator.userId).isEqualTo(user.uid)
        Assertions.assertThat(savedCreator.mainSubject).isEqualTo(mainSubject)
        Assertions.assertThat(savedCreator.introduction).isEqualTo(introduction)
    }
}
