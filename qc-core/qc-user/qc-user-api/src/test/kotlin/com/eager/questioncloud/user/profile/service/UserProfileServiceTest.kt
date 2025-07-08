package com.eager.questioncloud.user.profile.service

import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class UserProfileServiceTest(
    @Autowired val userProfileService: UserProfileService,
    @Autowired val userRepository: UserRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `사용자의 이름과 프로필 이미지를 업데이트할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "test@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )

        val newName = "업데이트된 이름"
        val newProfileImage = "https://example.com/new-profile.jpg"

        // when
        userProfileService.updateUserInformation(user.uid, newName, newProfileImage)

        // then
        val updatedUser = userRepository.getUser(user.uid)

        assertThat(updatedUser.userInformation.name).isEqualTo(newName)
        assertThat(updatedUser.userInformation.profileImage).isEqualTo(newProfileImage)
    }
}
