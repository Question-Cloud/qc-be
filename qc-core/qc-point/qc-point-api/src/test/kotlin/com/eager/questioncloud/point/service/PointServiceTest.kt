package com.eager.questioncloud.point.service

import com.eager.questioncloud.point.domain.UserPoint
import com.eager.questioncloud.point.repository.UserPointRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class PointServiceTest(
    @Autowired val pointService: PointService,
    @Autowired val userPointRepository: UserPointRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `보유 포인트를 조회할 수 있다`() {
        // given
        val userId = 1L
        userPointRepository.save(UserPoint.create(userId))
        val pointAmount = 10000
        userPointRepository.chargePoint(userId, pointAmount)
        
        // when
        val userPoint = pointService.getUserPoint(userId)
        
        // then
        Assertions.assertThat(userPoint.userId).isEqualTo(userId)
        Assertions.assertThat(userPoint.point).isEqualTo(pointAmount)
    }
}