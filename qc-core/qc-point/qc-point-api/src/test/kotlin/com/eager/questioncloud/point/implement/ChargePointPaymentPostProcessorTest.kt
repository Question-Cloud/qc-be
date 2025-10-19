package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.pg.PGConfirmResult
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.domain.UserPoint
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.repository.ChargePointPaymentIdempotentInfoRepository
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import com.eager.questioncloud.point.repository.UserPointRepository
import com.eager.questioncloud.utils.DBCleaner
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
class ChargePointPaymentPostProcessorTest(
    private val chargePointPaymentPostProcessor: ChargePointPaymentPostProcessor,
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val chargePointPaymentIdempotentInfoRepository: ChargePointPaymentIdempotentInfoRepository,
    private val userPointRepository: UserPointRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("PG 결제 승인 실패 결과를 전달받았을 때") {
            val userId = 1L
            val orderId = UUID.randomUUID().toString()
            val paymentId = UUID.randomUUID().toString()
            val chargePointType = ChargePointType.PackageA
            
            userPointRepository.save(UserPoint.create(userId))
            
            val chargePointPayment = ChargePointPayment(
                orderId = orderId,
                paymentId = paymentId,
                userId = userId,
                chargePointType = chargePointType,
                chargePointPaymentStatus = ChargePointPaymentStatus.PENDING_PG_PAYMENT
            )
            
            chargePointPaymentRepository.save(chargePointPayment)
            
            val pgConfirmResult = PGConfirmResult.Fail(orderId, paymentId)
            
            When("postProcess를 실행하면") {
                val result = chargePointPaymentPostProcessor.postProcess(chargePointPayment, pgConfirmResult)
                
                Then("결제 실패 처리가 되고, 멱등성 정보가 저장된다.") {
                    val updatedPayment = chargePointPaymentRepository.findByOrderId(orderId)
                    updatedPayment.chargePointPaymentStatus shouldBe ChargePointPaymentStatus.FAILED
                    result shouldBe ChargePointPaymentStatus.FAILED
                    
                    val idempotentInfo = chargePointPaymentIdempotentInfoRepository.findByOrderId(orderId)
                    idempotentInfo shouldNotBe null
                    idempotentInfo!!.orderId shouldBe orderId
                    idempotentInfo.paymentId shouldBe paymentId
                    idempotentInfo.chargePointPaymentStatus shouldBe ChargePointPaymentStatus.FAILED
                }
            }
        }
        
        Given("PG 결제 승인 성공 결과를 전달받았을 때") {
            val userId = 1L
            val orderId = UUID.randomUUID().toString()
            val paymentId = UUID.randomUUID().toString()
            val chargePointType = ChargePointType.PackageA
            
            userPointRepository.save(UserPoint.create(userId))
            
            val chargePointPayment = ChargePointPayment(
                orderId = orderId,
                paymentId = paymentId,
                userId = userId,
                chargePointType = chargePointType,
                chargePointPaymentStatus = ChargePointPaymentStatus.PENDING_PG_PAYMENT
            )
            
            chargePointPaymentRepository.save(chargePointPayment)
            
            val pgConfirmResult = PGConfirmResult.Success(orderId, paymentId)
            
            When("postProcess를 실행하면") {
                val result = chargePointPaymentPostProcessor.postProcess(chargePointPayment, pgConfirmResult)
                
                Then("충전 처리가 되고, 멱등성 정보가 저장된다.") {
                    val updatedPayment = chargePointPaymentRepository.findByOrderId(orderId)
                    updatedPayment.chargePointPaymentStatus shouldBe ChargePointPaymentStatus.CHARGED
                    result shouldBe ChargePointPaymentStatus.CHARGED
                    
                    val idempotentInfo = chargePointPaymentIdempotentInfoRepository.findByOrderId(orderId)
                    idempotentInfo shouldNotBe null
                    idempotentInfo!!.orderId shouldBe orderId
                    idempotentInfo.paymentId shouldBe paymentId
                    idempotentInfo.chargePointPaymentStatus shouldBe ChargePointPaymentStatus.CHARGED
                    
                    val userPoint = userPointRepository.getUserPoint(userId)
                    userPoint.point shouldBe chargePointType.amount
                }
            }
        }
        
        Given("동시에 결제를 처리하려고 할 때") {
            val userId = 1L
            val orderId = UUID.randomUUID().toString()
            val paymentId = UUID.randomUUID().toString()
            val chargePointType = ChargePointType.PackageA
            
            userPointRepository.save(UserPoint.create(userId))
            
            val chargePointPayment = ChargePointPayment(
                orderId = orderId,
                paymentId = paymentId,
                userId = userId,
                chargePointType = chargePointType,
                chargePointPaymentStatus = ChargePointPaymentStatus.PENDING_PG_PAYMENT
            )
            
            chargePointPaymentRepository.save(chargePointPayment)
            
            val pgConfirmResult = PGConfirmResult.Success(orderId, paymentId)
            
            When("동시에 postProcess를 호출하면") {
                val threadCount = 100
                val threads = (1..threadCount).map {
                    Thread {
                        chargePointPaymentPostProcessor.postProcess(chargePointPayment, pgConfirmResult)
                    }
                }
                
                threads.forEach { it.start() }
                threads.forEach { it.join() }
                
                Then("멱등성이 보장되어 포인트가 정확히 1번만 충전된다") {
                    val userPoint = userPointRepository.getUserPoint(userId)
                    userPoint.point shouldBe chargePointType.amount
                    
                    val updatedPayment = chargePointPaymentRepository.findByOrderId(orderId)
                    updatedPayment.chargePointPaymentStatus shouldBe ChargePointPaymentStatus.CHARGED
                    
                    val idempotentInfo = chargePointPaymentIdempotentInfoRepository.findByOrderId(orderId)
                    idempotentInfo shouldNotBe null
                    idempotentInfo!!.chargePointPaymentStatus shouldBe ChargePointPaymentStatus.CHARGED
                }
            }
        }
    }
}