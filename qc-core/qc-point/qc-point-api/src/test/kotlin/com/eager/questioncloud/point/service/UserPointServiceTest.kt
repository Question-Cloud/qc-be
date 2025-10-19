package com.eager.questioncloud.point.service

import com.eager.questioncloud.point.domain.UserPoint
import com.eager.questioncloud.point.implement.UserPointReader
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserPointServiceTest : BehaviorSpec() {
    private val userPointReader = mockk<UserPointReader>()
    private val userPointService = UserPointService(userPointReader)
    
    init {
        afterEach {
            clearAllMocks()
        }
        
        Given("유저 포인트 조회") {
            val userId = 100L
            val pointAmount = 10000
            val userPoint = UserPoint.create(userId).apply {
                charge(pointAmount)
            }
            
            every { userPointReader.getUserPoint(userId) } returns userPoint
            
            When("보유 포인트를 조회하면") {
                val result = userPointService.getUserPoint(userId)
                
                Then("포인트 정보가 반환된다") {
                    result.userId shouldBe userId
                    result.point shouldBe pointAmount
                    verify(exactly = 1) { userPointReader.getUserPoint(userId) }
                }
            }
        }
    }
}