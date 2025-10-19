package com.eager.questioncloud.point.service

import com.eager.questioncloud.common.pg.PGConfirmResult
import com.eager.questioncloud.common.pg.PGPayment
import com.eager.questioncloud.common.pg.PGPaymentStatus
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.implement.*
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.*

class ChargePointPaymentServiceTest : BehaviorSpec() {
    private val chargePointPaymentReader = mockk<ChargePointPaymentReader>()
    private val chargePointPaymentPreparer = mockk<ChargePointPaymentPreparer>()
    private val chargePointPaymentAppender = mockk<ChargePointPaymentAppender>()
    private val chargePointPaymentPostProcessor = mockk<ChargePointPaymentPostProcessor>()
    private val chargePointPaymentPGProcessor = mockk<ChargePointPaymentPGProcessor>()
    private val chargePointPaymentIdempotentInfoReader = mockk<ChargePointPaymentIdempotentInfoReader>()
    
    private val chargePointPaymentService = ChargePointPaymentService(
        chargePointPaymentReader,
        chargePointPaymentPreparer,
        chargePointPaymentAppender,
        chargePointPaymentPostProcessor,
        chargePointPaymentPGProcessor,
        chargePointPaymentIdempotentInfoReader
    )
    
    init {
        afterEach {
            clearAllMocks()
        }
        
        Given("주문 생성 요청이 들어왔을 때") {
            val userId = 100L
            val chargePointType = ChargePointType.PackageA
            val expectedOrderId = UUID.randomUUID().toString()
            val order = ChargePointPayment(
                orderId = expectedOrderId,
                paymentId = UUID.randomUUID().toString(),
                userId = userId,
                chargePointType = chargePointType,
                chargePointPaymentStatus = ChargePointPaymentStatus.ORDERED
            )
            
            every { chargePointPaymentAppender.createOrder(userId, chargePointType) } returns order
            
            When("주문을 생성하면") {
                val result = chargePointPaymentService.createOrder(userId, chargePointType)
                
                Then("주문 ID가 반환된다") {
                    result shouldBe expectedOrderId
                    verify(exactly = 1) { chargePointPaymentAppender.createOrder(userId, chargePointType) }
                }
            }
        }
        
        Given("결제 승인 요청이 들어왔을 때") {
            val orderId = UUID.randomUUID().toString()
            val paymentId = UUID.randomUUID().toString()
            val amount = 10000
            val chargePointType = ChargePointType.PackageA
            
            val pgPayment = PGPayment(
                paymentId = paymentId,
                orderId = orderId,
                amount = amount,
                status = PGPaymentStatus.READY
            )
            
            val chargePointPayment = ChargePointPayment(
                orderId = orderId,
                paymentId = paymentId,
                userId = 100L,
                chargePointType = chargePointType,
                chargePointPaymentStatus = ChargePointPaymentStatus.PENDING_PG_PAYMENT
            )
            
            val confirmResult = PGConfirmResult.Success(orderId, paymentId)
            
            every { chargePointPaymentIdempotentInfoReader.getOrderId(orderId) } returns null
            every { chargePointPaymentPGProcessor.getPayment(orderId) } returns pgPayment
            every { chargePointPaymentPreparer.prepare(pgPayment) } returns chargePointPayment
            every { chargePointPaymentPGProcessor.confirm(any()) } returns confirmResult
            every {
                chargePointPaymentPostProcessor.postProcess(
                    chargePointPayment,
                    confirmResult
                )
            } returns ChargePointPaymentStatus.CHARGED
            
            When("결제를 승인하면") {
                val result = chargePointPaymentService.approvePayment(orderId)
                
                Then("결제 승인 플로우가 정상적으로 처리되어 CHARGED 상태가 반환된다") {
                    result shouldBe ChargePointPaymentStatus.CHARGED
                    verify(exactly = 1) { chargePointPaymentIdempotentInfoReader.getOrderId(orderId) }
                    verify(exactly = 1) { chargePointPaymentPGProcessor.getPayment(orderId) }
                    verify(exactly = 1) { chargePointPaymentPreparer.prepare(pgPayment) }
                    verify(exactly = 1) { chargePointPaymentPGProcessor.confirm(any()) }
                    verify(exactly = 1) { chargePointPaymentPostProcessor.postProcess(chargePointPayment, confirmResult) }
                }
            }
        }
        
        Given("결제 완료 여부 조회 요청이 들어왔을 때") {
            val userId = 100L
            val orderId = UUID.randomUUID().toString()
            
            every { chargePointPaymentReader.isCompletedPayment(userId, orderId) } returns true
            
            When("결제 완료 여부를 조회하면") {
                val result = chargePointPaymentService.isCompletePayment(userId, orderId)
                
                Then("결제 완료 여부가 반환된다") {
                    result shouldBe true
                    verify(exactly = 1) { chargePointPaymentReader.isCompletedPayment(userId, orderId) }
                }
            }
        }
    }
}
