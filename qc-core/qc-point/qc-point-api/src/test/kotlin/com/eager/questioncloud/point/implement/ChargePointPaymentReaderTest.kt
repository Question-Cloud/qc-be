package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class ChargePointPaymentReaderTest(
    private val chargePointPaymentReader: ChargePointPaymentReader,
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("포인트 충전 결제 내역 조회") {
            val userId = 100L
            val count = 10
            
            val expectedPayments = (1..count).map {
                chargePointPaymentRepository.save(
                    ChargePointPayment(
                        orderId = UUID.randomUUID().toString(),
                        paymentId = UUID.randomUUID().toString(),
                        userId = userId,
                        chargePointType = ChargePointType.PackageA,
                        chargePointPaymentStatus = ChargePointPaymentStatus.CHARGED
                    )
                )
            }
            
            When("결제 내역을 조회하면") {
                val pagingInformation = PagingInformation(0, 10)
                val result = chargePointPaymentReader.getChargePointPayment(userId, pagingInformation)
                
                Then("결제 내역이 조회된다") {
                    result shouldHaveSize count
                    
                    val expectedOrderIds = expectedPayments.map { it.orderId }
                    val resultOrderIds = result.map { it.orderId }
                    
                    resultOrderIds shouldContainExactlyInAnyOrder expectedOrderIds
                }
            }
        }
        
        Given("포인트 충전 내역 개수 조회") {
            val userId = 200L
            val count = 10
            (1..count).forEach { _ ->
                chargePointPaymentRepository.save(
                    ChargePointPayment(
                        orderId = UUID.randomUUID().toString(),
                        paymentId = UUID.randomUUID().toString(),
                        userId = userId,
                        chargePointType = ChargePointType.PackageA,
                        chargePointPaymentStatus = ChargePointPaymentStatus.CHARGED
                    )
                )
            }
            
            When("결제 내역 개수를 조회하면") {
                val result = chargePointPaymentReader.countChargePointPayment(userId)
                
                Then("결제 내역 개수가 조회된다.") {
                    result shouldBe count
                }
            }
        }
    }
}
