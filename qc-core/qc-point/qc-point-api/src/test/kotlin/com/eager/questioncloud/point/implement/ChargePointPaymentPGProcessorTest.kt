package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pg.PGConfirmRequest
import com.eager.questioncloud.common.pg.PGConfirmResult
import com.eager.questioncloud.common.pg.PGPayment
import com.eager.questioncloud.common.pg.PGPaymentStatus
import com.eager.questioncloud.common.pg.PaymentAPI
import com.eager.questioncloud.test.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class ChargePointPaymentPGProcessorTest(
    private val chargePointPaymentPGProcessor: ChargePointPaymentPGProcessor,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var paymentAPI: PaymentAPI
    
    init {
        afterEach {
            clearAllMocks()
            dbCleaner.cleanUp()
        }
        
        Given("PG 결제 내역 조회") {
            val orderId = UUID.randomUUID().toString()
            val paymentId = UUID.randomUUID().toString()
            val amount = 10000
            
            val pgPayment = PGPayment(
                paymentId = paymentId,
                orderId = orderId,
                amount = amount,
                status = PGPaymentStatus.DONE
            )
            
            every { paymentAPI.getPayment(orderId) } returns pgPayment
            
            When("orderId로 결제 내역을 조회하면") {
                val result = chargePointPaymentPGProcessor.getPayment(orderId)
                
                Then("결제 내역이 조회된다") {
                    result.orderId shouldBe orderId
                    result.paymentId shouldBe paymentId
                    result.amount shouldBe amount
                    result.status shouldBe PGPaymentStatus.DONE
                }
            }
        }
        
        Given("PG 결제 승인 - 성공") {
            val orderId = UUID.randomUUID().toString()
            val paymentId = UUID.randomUUID().toString()
            val amount = 10000
            
            val confirmRequest = PGConfirmRequest(
                paymentId = paymentId,
                orderId = orderId,
                amount = amount
            )
            
            every { paymentAPI.confirm(confirmRequest) } returns PGConfirmResult.Success(orderId, paymentId)
            
            When("PG 결제 승인을 요청하면") {
                val result = chargePointPaymentPGProcessor.confirm(confirmRequest)
                
                Then("PGConfirmResult.Success가 반환된다") {
                    result.shouldBeInstanceOf<PGConfirmResult.Success>()
                    result.orderId shouldBe orderId
                    result.paymentId shouldBe paymentId
                }
            }
        }
        
        Given("PG 결제 승인 - 실패") {
            val orderId = UUID.randomUUID().toString()
            val paymentId = UUID.randomUUID().toString()
            val amount = 10000
            
            val confirmRequest = PGConfirmRequest(
                paymentId = paymentId,
                orderId = orderId,
                amount = amount
            )
            
            every { paymentAPI.confirm(confirmRequest) } returns PGConfirmResult.Fail(orderId, paymentId)
            
            When("PG 결제 승인을 요청하면") {
                val result = chargePointPaymentPGProcessor.confirm(confirmRequest)
                
                Then("PGConfirmResult.Fail이 반환된다") {
                    result.shouldBeInstanceOf<PGConfirmResult.Fail>()
                    result.orderId shouldBe orderId
                    result.paymentId shouldBe paymentId
                }
            }
        }
        
        Given("PG 결제 승인 중 5번 연속 예외 발생") {
            val orderId = UUID.randomUUID().toString()
            val paymentId = UUID.randomUUID().toString()
            val amount = 10000
            
            val confirmRequest = PGConfirmRequest(
                paymentId = paymentId,
                orderId = orderId,
                amount = amount
            )
            
            every { paymentAPI.confirm(confirmRequest) } throws RuntimeException("PG 통신 실패")
            
            When("PG 결제 승인을 요청하면") {
                Then("5번 재시도 후 recover 메서드가 호출되어 CoreException(PAYMENT_ERROR)이 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        chargePointPaymentPGProcessor.confirm(confirmRequest)
                    }
                    exception.error shouldBe Error.PAYMENT_ERROR
                }
            }
        }
    }
}