package com.eager.questioncloud.core.domain.point.implement

import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class UserPointManagerTest(
    @Autowired
    private val userPointManager: UserPointManager,

    @Autowired
    private val userPointRepository: UserPointRepository,

    @Autowired
    private val userRepository: UserRepository,
) {
    @AfterEach
    fun tearDown() {
        userRepository.deleteAllInBatch()
        userPointRepository.deleteAllInBatch()
    }

    @Test
    @DisplayName("유저의 포인트를 초기화 한다.")
    fun init() {
        // given
        val user = userRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        // when
        userPointManager.init(user.uid!!)

        // then
        val userPoint = userPointRepository.getUserPoint(user.uid!!)
        Assertions.assertNotNull(userPoint)
    }

    @Test
    @DisplayName("유저의 포인트를 충전한다.")
    fun chargePoint() {
        // given
        val user = userRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )
        userPointManager.init(user.uid!!)

        // when
        userPointManager.chargePoint(user.uid!!, 10000)

        //then
        val userPoint = userPointRepository.getUserPoint(user.uid!!)
        org.assertj.core.api.Assertions.assertThat(userPoint.point).isEqualTo(10000)
    }

    @Test
    @DisplayName("유저의 포인트를 차감한다.")
    fun usePoint() {
        // given
        val user = userRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )
        userPointManager.init(user.uid!!)
        userPointManager.chargePoint(user.uid!!, 10000)

        // when
        userPointManager.usePoint(user.uid!!, 5000)

        //then
        val userPoint = userPointRepository.getUserPoint(user.uid!!)
        org.assertj.core.api.Assertions.assertThat(userPoint.point).isEqualTo(5000)
    }
}