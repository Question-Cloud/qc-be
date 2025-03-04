package com.eager.questioncloud.core.domain.point.implement;

import static org.assertj.core.api.Assertions.assertThat;

import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository;
import com.eager.questioncloud.core.domain.point.model.UserPoint;
import com.eager.questioncloud.core.domain.user.UserBuilder;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserPointManagerTest {
    @Autowired
    private UserPointManager userPointManager;

    @Autowired
    private UserPointRepository userPointRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        userPointRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("유저의 포인트를 초기화 한다.")
    void init() {
        // given
        User user = userRepository.save(UserBuilder.builder().build().toUser());

        // when
        userPointManager.init(user.getUid());

        // then
        UserPoint userPoint = userPointRepository.getUserPoint(user.getUid());
        Assertions.assertNotNull(userPoint);
    }

    @Test
    @DisplayName("유저의 포인트를 충전한다.")
    void chargePoint() {
        // given
        User user = userRepository.save(UserBuilder.builder().build().toUser());
        userPointManager.init(user.getUid());

        // when
        userPointManager.chargePoint(user.getUid(), 10000);

        //then
        UserPoint userPoint = userPointRepository.getUserPoint(user.getUid());
        assertThat(userPoint.getPoint()).isEqualTo(10000);
    }

    @Test
    @DisplayName("유저의 포인트를 차감한다.")
    void usePoint() {
        // given
        User user = userRepository.save(UserBuilder.builder().build().toUser());
        userPointManager.init(user.getUid());
        userPointManager.chargePoint(user.getUid(), 10000);

        // when
        userPointManager.usePoint(user.getUid(), 5000);

        //then
        UserPoint userPoint = userPointRepository.getUserPoint(user.getUid());
        assertThat(userPoint.getPoint()).isEqualTo(5000);
    }
}