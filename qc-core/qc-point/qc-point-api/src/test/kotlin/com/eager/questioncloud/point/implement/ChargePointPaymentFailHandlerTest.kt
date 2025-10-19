package com.eager.questioncloud.point.implement

import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.domain.ChargePointPaymentIdempotentInfo
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.repository.ChargePointPaymentIdempotentInfoRepository
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.SpykBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class ChargePointPaymentFailHandlerTest(
    private val chargePointPaymentFailHandler: ChargePointPaymentFailHandler,
    private val chargePointPaymentIdempotentInfoRepository: ChargePointPaymentIdempotentInfoRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @SpykBean
    private lateinit var chargePointPaymentRepository: ChargePointPaymentRepository
    
    init {
        afterEach {
            clearAllMocks()
            dbCleaner.cleanUp()
        }
        
        Given("결제 실패 처리가 필요한 상황일 때") {
            val userId = 1L
            val orderId = UUID.randomUUID().toString()
            val paymentId = UUID.randomUUID().toString()
            val chargePointType = ChargePointType.PackageA
            
            val chargePointPayment = ChargePointPayment(
                orderId = orderId,
                paymentId = paymentId,
                userId = userId,
                chargePointType = chargePointType,
                chargePointPaymentStatus = ChargePointPaymentStatus.PENDING_PG_PAYMENT
            )
            
            chargePointPaymentRepository.save(chargePointPayment)
            
            When("결제 실패 처리를 하면") {
                chargePointPaymentFailHandler.fail(orderId, paymentId)
                
                Then("결제 상태가 FAILED로 변경되고 멱등성 정보가 저장된다") {
                    val updatedPayment = chargePointPaymentRepository.findByOrderId(orderId)
                    updatedPayment.chargePointPaymentStatus shouldBe ChargePointPaymentStatus.FAILED
                    
                    val idempotentInfo = chargePointPaymentIdempotentInfoRepository.findByOrderId(orderId)
                    idempotentInfo shouldNotBe null
                    idempotentInfo!!.paymentId shouldBe paymentId
                    idempotentInfo.chargePointPaymentStatus shouldBe ChargePointPaymentStatus.FAILED
                }
            }
        }
        
        Given("이미 실패 처리된 결제가 있을 때") {
            val userId = 1L
            val orderId = UUID.randomUUID().toString()
            val paymentId = UUID.randomUUID().toString()
            val chargePointType = ChargePointType.PackageA
            
            val chargePointPayment = ChargePointPayment(
                orderId = orderId,
                paymentId = paymentId,
                userId = userId,
                chargePointType = chargePointType,
                chargePointPaymentStatus = ChargePointPaymentStatus.FAILED
            )
            chargePointPaymentRepository.save(chargePointPayment)
            
            chargePointPaymentIdempotentInfoRepository.insert(
                ChargePointPaymentIdempotentInfo(
                    orderId,
                    paymentId,
                    ChargePointPaymentStatus.FAILED
                )
            )
            
            When("중복으로 실패 처리를 요청하면") {
                chargePointPaymentFailHandler.fail(orderId, paymentId)
                
                Then("멱등성이 보장되어 update 메서드가 호출되지 않는다") {
                    verify(exactly = 0) { chargePointPaymentRepository.update(any()) }
                }
            }
        }
    }
}