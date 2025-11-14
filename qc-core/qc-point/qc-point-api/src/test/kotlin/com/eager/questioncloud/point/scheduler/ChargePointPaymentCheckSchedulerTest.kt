package com.eager.questioncloud.point.scheduler

import com.eager.questioncloud.common.pg.PGConfirmRequest
import com.eager.questioncloud.common.pg.PGConfirmResult
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.implement.ChargePointPaymentPGProcessor
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import com.eager.questioncloud.test.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class ChargePointPaymentCheckSchedulerTest(
    private val chargePointPaymentCheckScheduler: ChargePointPaymentCheckScheduler,
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var chargePointPaymentPGProcessor: ChargePointPaymentPGProcessor
    
    init {
        afterEach {
            clearAllMocks()
            dbCleaner.cleanUp()
        }
        
        Given("10분 전에 요청된 PENDING_PG_PAYMENT 상태의 결제 건들이 있을 때") {
            (1..10).forEach { _ ->
                chargePointPaymentRepository.save(
                    ChargePointPayment(
                        paymentId = UUID.randomUUID().toString(),
                        userId = 1L,
                        chargePointType = ChargePointType.PackageB,
                        chargePointPaymentStatus = ChargePointPaymentStatus.PENDING_PG_PAYMENT,
                        requestAt = LocalDateTime.now().minusMinutes(10)
                    )
                )
            }
            
            every { chargePointPaymentPGProcessor.confirm(any()) } answers {
                val request = it.invocation.args[0] as PGConfirmRequest
                PGConfirmResult.Success(request.orderId, request.paymentId)
            }
            
            When("스케줄러 task를 실행하면") {
                chargePointPaymentCheckScheduler.chargePointPaymentCheckScheduler()
                
                Then("모든 PENDING 결제 건이 처리되어 0건이 된다") {
                    val remainingPendingPayments = chargePointPaymentRepository.getPendingPayments()
                    remainingPendingPayments.size shouldBe 0
                }
            }
        }
    }
}