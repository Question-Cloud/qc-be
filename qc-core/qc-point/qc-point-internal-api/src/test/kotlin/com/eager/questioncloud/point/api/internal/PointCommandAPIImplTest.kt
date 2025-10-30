package com.eager.questioncloud.point.api.internal

import com.eager.questioncloud.point.domain.UserPoint
import com.eager.questioncloud.point.repository.UserPointRepository
import com.eager.questioncloud.test.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class PointCommandAPIImplTest(
    private val pointCommandAPIImpl: PointCommandAPIImpl,
    private val userPointRepository: UserPointRepository,
    private val dbCleaner: DBCleaner
) : BehaviorSpec() {

    init {
        afterTest {
            dbCleaner.cleanUp()
        }

        Given("사용자가 존재할 때") {
            val userId = 1L

            When("포인트 초기화를 실행하면") {
                pointCommandAPIImpl.initialize(userId)

                Then("사용자 포인트가 0으로 초기화된다") {
                    val userPoint = userPointRepository.getUserPoint(userId)
                    userPoint shouldNotBe null
                    userPoint.userId shouldBe userId
                    userPoint.point shouldBe 0
                }
            }
        }

        Given("사용자에게 포인트가 있을 때") {
            val userId = 2L
            val initPointAmount = 100000
            val usagePointAmount = 1000
            userPointRepository.save(UserPoint(userId, initPointAmount))

            When("포인트를 사용하면") {
                pointCommandAPIImpl.usePoint(userId, usagePointAmount)

                Then("사용한 만큼 포인트가 차감된다") {
                    val userPoint = userPointRepository.getUserPoint(userId)
                    userPoint.point shouldBe (initPointAmount - usagePointAmount)
                }
            }
        }
    }
}