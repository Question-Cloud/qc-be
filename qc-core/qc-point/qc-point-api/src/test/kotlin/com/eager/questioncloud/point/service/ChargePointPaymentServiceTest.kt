package com.eager.questioncloud.point.service

import com.eager.questioncloud.common.pg.PGConfirmResponse
import com.eager.questioncloud.common.pg.PGPayment
import com.eager.questioncloud.common.pg.PGPaymentStatus
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.domain.UserPoint
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.implement.ChargePointPaymentPGProcessor
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import com.eager.questioncloud.point.repository.UserPointRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class ChargePointPaymentServiceTest(
    @Autowired val chargePointPaymentService: ChargePointPaymentService,
    @Autowired val chargePointPaymentRepository: ChargePointPaymentRepository,
    @Autowired val userPointRepository: UserPointRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @MockBean
    lateinit var chargePointPaymentPGProcessor: ChargePointPaymentPGProcessor
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `포인트 충전 주문을 생성할 수 있다`() {
        // given
        val userId = 1L
        val chargePointType = ChargePointType.PackageB
        
        // when
        val orderId = chargePointPaymentService.createOrder(userId, chargePointType)
        
        // then
        val order = chargePointPaymentRepository.findByOrderId(orderId)
        
        Assertions.assertThat(order).isNotNull
        Assertions.assertThat(order.chargePointType).isEqualTo(chargePointType)
        Assertions.assertThat(order.chargePointPaymentStatus).isEqualTo(ChargePointPaymentStatus.ORDERED)
    }
    
    @Test
    fun `포인트 충전을 처리할 수 있다`() {
        // given
        val userId = 1L
        userPointRepository.save(UserPoint.create(userId))
        
        val chargePointType = ChargePointType.PackageB
        val order = chargePointPaymentRepository.save(ChargePointPayment.createOrder(userId, chargePointType))
        
        given(chargePointPaymentPGProcessor.getPayment(any())).willReturn(
            PGPayment("paymentId", order.orderId, chargePointType.amount, PGPaymentStatus.READY)
        )
        
        whenever(chargePointPaymentPGProcessor.confirm(any())).thenReturn(PGConfirmResponse(PGPaymentStatus.DONE))
        
        // when
        chargePointPaymentService.approvePayment(order.orderId)
        
        // then
        val userPoint = userPointRepository.getUserPoint(userId)
        Assertions.assertThat(userPoint.point).isEqualTo(chargePointType.amount)
    }
    
    @Test
    fun `포인트 충전 완료 여부를 조회 할 수 있다`() {
        // given
        val userId = 1L
        val chargePointType = ChargePointType.PackageB
        
        val order1 = ChargePointPayment.createOrder(userId, chargePointType)
        order1.charge()
        chargePointPaymentRepository.save(order1)
        
        val order2 = ChargePointPayment.createOrder(userId, chargePointType)
        chargePointPaymentRepository.save(order2)
        
        // when
        val order1Status = chargePointPaymentService.isCompletePayment(userId, order1.orderId)
        val order2Status = chargePointPaymentService.isCompletePayment(userId, order2.orderId)
        
        Assertions.assertThat(order1Status).isTrue()
        Assertions.assertThat(order2Status).isFalse()
    }
    
    @Test
    fun `결제 승인 대기 중인 건이 다시 요청오면 이어서 처리할 수 있다`() {
        // given
        val userId = 1L
        userPointRepository.save(UserPoint.create(userId))
        
        val chargePointType = ChargePointType.PackageA
        val chargePointPayment = ChargePointPayment(
            orderId = UUID.randomUUID().toString(),
            paymentId = UUID.randomUUID().toString(),
            userId = userId,
            chargePointType = chargePointType,
            chargePointPaymentStatus = ChargePointPaymentStatus.PENDING_PG_PAYMENT,
            createdAt = LocalDateTime.now(),
            requestAt = LocalDateTime.now(),
        )
        chargePointPaymentRepository.save(chargePointPayment)
        
        whenever(chargePointPaymentPGProcessor.getPayment(any())).thenAnswer { e ->
            val orderId = e.getArgument<String>(0)
            PGPayment(UUID.randomUUID().toString(), orderId, ChargePointType.PackageA.amount, PGPaymentStatus.DONE)
        }
        
        whenever(chargePointPaymentPGProcessor.confirm(any())).thenReturn(PGConfirmResponse(PGPaymentStatus.DONE))
        
        // when
        chargePointPaymentService.approvePayment(chargePointPayment.orderId)
        
        // then
        val userPoint = userPointRepository.getUserPoint(userId)
        Assertions.assertThat(userPoint.point).isEqualTo(chargePointType.amount)
    }
}