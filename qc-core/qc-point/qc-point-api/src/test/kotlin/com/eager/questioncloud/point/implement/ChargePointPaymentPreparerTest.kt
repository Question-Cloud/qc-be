package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.pg.PGPayment
import com.eager.questioncloud.common.pg.PGPaymentStatus
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import com.eager.questioncloud.utils.DBCleaner
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
class ChargePointPaymentPreparerTest(
    private val chargePointPaymentPreparer: ChargePointPaymentPreparer,
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("유효한 PG 결제 정보가 주어졌을 때") {
            val userId = 1L
            val paymentId = "payment-12345"
            val order = chargePointPaymentRepository.save(
                ChargePointPayment.createOrder(userId, ChargePointType.PackageA)
            )
            val pgPayment = PGPayment(
                paymentId = paymentId,
                orderId = order.orderId,
                amount = ChargePointType.PackageA.amount,
                status = PGPaymentStatus.READY
            )
            
            When("결제 준비를 실행하면") {
                val result = chargePointPaymentPreparer.prepare(pgPayment)
                
                Then("ChargePointPayment 상태가 PENDING_PG_PAYMENT로 변경된다") {
                    result.chargePointPaymentStatus shouldBe ChargePointPaymentStatus.PENDING_PG_PAYMENT
                    result.paymentId shouldBe paymentId
                    
                    val savedChargePointPayment = chargePointPaymentRepository.findByOrderId(order.orderId)
                    savedChargePointPayment.chargePointPaymentStatus shouldBe ChargePointPaymentStatus.PENDING_PG_PAYMENT
                    savedChargePointPayment.paymentId shouldNotBe null
                    savedChargePointPayment.paymentId shouldBe paymentId
                }
            }
        }
    }
}