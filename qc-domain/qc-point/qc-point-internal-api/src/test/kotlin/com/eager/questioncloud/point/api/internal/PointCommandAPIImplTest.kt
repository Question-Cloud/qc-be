package com.eager.questioncloud.point.api.internal

import com.eager.questioncloud.point.domain.UserPoint
import com.eager.questioncloud.point.infrastructure.repository.UserPointRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class PointCommandAPIImplTest(
    @Autowired val pointCommandAPIImpl: PointCommandAPIImpl,
    @Autowired val userPointRepository: UserPointRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `포인트 초기화를 할 수 있다`() {
        // given
        val userId = 1L
        
        // when
        pointCommandAPIImpl.initialize(userId)
        
        // then
        val userPoint = userPointRepository.getUserPoint(userId)
        Assertions.assertThat(userPoint).isNotNull
        Assertions.assertThat(userPoint.userId).isEqualTo(userId)
        Assertions.assertThat(userPoint.point).isEqualTo(0)
    }
    
    @Test
    fun `포인트 사용을 할 수 있다`() {
        // given
        val userId = 2L
        val initPointAmount = 100000
        val usagePointAmount = 1000
        userPointRepository.save(UserPoint(userId, initPointAmount))
        
        // when
        pointCommandAPIImpl.usePoint(userId, usagePointAmount)
        
        // then
        val userPoint = userPointRepository.getUserPoint(userId)
        Assertions.assertThat(userPoint.point).isEqualTo(initPointAmount - usagePointAmount)
    }
}