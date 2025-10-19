package com.eager.questioncloud.point.implement

import com.eager.questioncloud.point.domain.UserPoint
import com.eager.questioncloud.point.repository.UserPointRepository
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class UserPointReaderTest(
    private val userPointReader: UserPointReader,
    private val userPointRepository: UserPointRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("유저 보유 포인트 조회") {
            val userId = 100L
            val pointAmount = 10000
            
            userPointRepository.save(UserPoint.create(userId))
            userPointRepository.chargePoint(userId, pointAmount)
            
            When("보유 포인트를 조회하면") {
                val result = userPointReader.getUserPoint(userId)
                
                Then("포인트 정보가 조회된다") {
                    result.userId shouldBe userId
                    result.point shouldBe pointAmount
                }
            }
        }
    }
}