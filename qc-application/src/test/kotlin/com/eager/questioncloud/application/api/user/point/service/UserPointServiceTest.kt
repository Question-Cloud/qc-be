package com.eager.questioncloud.application.api.user.point.service

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserPointFixtureHelper
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository
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
class UserPointServiceTest(
    @Autowired val userPointService: UserPointService,
    @Autowired val userPointRepository: UserPointRepository,
    @Autowired val userRepository: UserRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `사용자의 포인트를 조회할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "test@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        
        val expectedPoint = 1500
        UserPointFixtureHelper.createUserPoint(user.uid, expectedPoint, userPointRepository)

        // when
        val userPoint = userPointService.getUserPoint(user.uid)

        // then
        Assertions.assertThat(userPoint.userId).isEqualTo(user.uid)
        Assertions.assertThat(userPoint.point).isEqualTo(expectedPoint)
    }
}
