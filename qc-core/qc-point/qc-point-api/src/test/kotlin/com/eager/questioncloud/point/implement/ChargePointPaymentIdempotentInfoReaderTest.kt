package com.eager.questioncloud.point.implement

import com.eager.questioncloud.point.domain.ChargePointPaymentIdempotentInfo
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.repository.ChargePointPaymentIdempotentInfoRepository
import com.eager.questioncloud.test.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class ChargePointPaymentIdempotentInfoReaderTest(
    private val chargePointPaymentIdempotentInfoReader: ChargePointPaymentIdempotentInfoReader,
    private val chargePointPaymentIdempotentInfoRepository: ChargePointPaymentIdempotentInfoRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("결제 실패 멱등성 정보가 저장되어 있을 때") {
            val orderId = UUID.randomUUID().toString()
            val paymentId = UUID.randomUUID().toString()
            
            val idempotentInfo = ChargePointPaymentIdempotentInfo(
                orderId = orderId,
                paymentId = paymentId,
                chargePointPaymentStatus = ChargePointPaymentStatus.FAILED
            )
            
            chargePointPaymentIdempotentInfoRepository.insert(idempotentInfo)
            
            When("orderId로 멱등성 정보를 조회하면") {
                val result = chargePointPaymentIdempotentInfoReader.getOrderId(orderId)
                
                Then("멱등성 정보가 조회된다") {
                    result shouldNotBe null
                    result!!.orderId shouldBe orderId
                    result.paymentId shouldBe paymentId
                    result.chargePointPaymentStatus shouldBe ChargePointPaymentStatus.FAILED
                }
            }
        }
        
        Given("결제 성공 멱등성 정보가 저장되어 있을 때") {
            val orderId = UUID.randomUUID().toString()
            val paymentId = UUID.randomUUID().toString()
            
            val idempotentInfo = ChargePointPaymentIdempotentInfo(
                orderId = orderId,
                paymentId = paymentId,
                chargePointPaymentStatus = ChargePointPaymentStatus.CHARGED
            )
            
            chargePointPaymentIdempotentInfoRepository.insert(idempotentInfo)
            
            When("orderId로 멱등성 정보를 조회하면") {
                val result = chargePointPaymentIdempotentInfoReader.getOrderId(orderId)
                
                Then("멱등성 정보가 조회된다") {
                    result shouldNotBe null
                    result!!.orderId shouldBe orderId
                    result.paymentId shouldBe paymentId
                    result.chargePointPaymentStatus shouldBe ChargePointPaymentStatus.CHARGED
                }
            }
        }
        
        Given("멱등성 정보가 존재하지 않을 때") {
            val orderId = UUID.randomUUID().toString()
            
            When("orderId로 멱등성 정보를 조회하면") {
                val result = chargePointPaymentIdempotentInfoReader.getOrderId(orderId)
                
                Then("null이 반환된다") {
                    result shouldBe null
                }
            }
        }
    }
}
