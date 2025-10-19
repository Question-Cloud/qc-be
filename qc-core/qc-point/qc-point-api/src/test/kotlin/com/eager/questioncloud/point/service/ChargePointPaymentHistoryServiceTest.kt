package com.eager.questioncloud.point.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.implement.ChargePointPaymentReader
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import java.util.*

class ChargePointPaymentHistoryServiceTest : BehaviorSpec() {
    private val chargePointPaymentReader = mockk<ChargePointPaymentReader>()
    private val chargePointPaymentHistoryService = ChargePointPaymentHistoryService(chargePointPaymentReader)
    
    init {
        afterEach {
            clearAllMocks()
        }
        
        Given("사용자의 포인트 충전 결제 내역이 있을 때") {
            val userId = 100L
            val pagingInformation = PagingInformation(0, 10)
            
            val payments = (1..10).map {
                createChargedPayment(userId)
            }
            
            every { chargePointPaymentReader.getChargePointPayment(userId, pagingInformation) } returns payments
            
            When("결제 내역을 조회하면") {
                val result = chargePointPaymentHistoryService.getChargePointPaymentHistory(userId, pagingInformation)
                
                Then("모든 결제 내역이 조회된다") {
                    result shouldHaveSize payments.size
                }
            }
        }
        
        Given("사용자의 포인트 충전 결제 내역이 존재할 때") {
            val userId = 102L
            
            val payments = (1..10).map {
                createChargedPayment(userId)
            }
            
            every { chargePointPaymentReader.countChargePointPayment(userId) } returns payments.size
            
            When("결제 내역 개수를 조회하면") {
                val result = chargePointPaymentHistoryService.countChargePointPayment(userId)
                
                Then("결제 내역 개수가 조회된다") {
                    result shouldBe payments.size
                }
            }
        }
    }
}

private fun createChargedPayment(userId: Long): ChargePointPayment {
    return ChargePointPayment(
        orderId = UUID.randomUUID().toString(),
        paymentId = UUID.randomUUID().toString(),
        userId = userId,
        chargePointType = ChargePointType.entries.toTypedArray().random(),
        chargePointPaymentStatus = ChargePointPaymentStatus.CHARGED
    )
}
