package com.eager.questioncloud.point.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository
import com.eager.questioncloud.utils.DBCleaner
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class ChargePointPaymentHistoryServiceTest(
    @Autowired val chargePointPaymentHistoryService: ChargePointPaymentHistoryService,
    @Autowired val chargePointPaymentRepository: ChargePointPaymentRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `포인트 충전 결제 내역을 조회할 수 있다`() {
        //given
        val userId = 100L
        
        val payment1 = createChargedPayment(userId, ChargePointType.PackageA)
        val payment2 = createChargedPayment(userId, ChargePointType.PackageB)
        val payment3 = createChargedPayment(userId, ChargePointType.PackageC)
        
        chargePointPaymentRepository.save(payment1)
        chargePointPaymentRepository.save(payment2)
        chargePointPaymentRepository.save(payment3)
        
        val pagingInformation = PagingInformation(0, 10)
        
        //when
        val result = chargePointPaymentHistoryService.getChargePointPayments(userId, pagingInformation)
        
        //then
        Assertions.assertThat(result).hasSize(3)
        
        val amounts = result.map { it.chargePointType.amount }
        Assertions.assertThat(amounts).containsExactlyInAnyOrder(1000, 5000, 10000)
        
        result.forEach { payment ->
            Assertions.assertThat(payment.userId).isEqualTo(userId)
            Assertions.assertThat(payment.chargePointPaymentStatus).isEqualTo(ChargePointPaymentStatus.CHARGED)
        }
    }
    
    @Test
    fun `포인트 충전 결제 내역의 개수를 조회할 수 있다`() {
        //given
        val userId = 102L
        
        val payment1 = createChargedPayment(userId, ChargePointType.PackageD)
        val payment2 = createChargedPayment(userId, ChargePointType.PackageE)
        val payment3 = createChargedPayment(userId, ChargePointType.PackageF)
        val payment4 = createChargedPayment(userId, ChargePointType.PackageA)
        chargePointPaymentRepository.save(payment1)
        chargePointPaymentRepository.save(payment2)
        chargePointPaymentRepository.save(payment3)
        chargePointPaymentRepository.save(payment4)
        
        //when
        val result = chargePointPaymentHistoryService.countChargePointPayment(userId)
        
        //then
        Assertions.assertThat(result).isEqualTo(4)
    }
    
    
    private fun createChargedPayment(userId: Long, chargePointType: ChargePointType): ChargePointPayment {
        val payment = ChargePointPayment.createOrder(userId, chargePointType)
        payment.prepare(RandomStringUtils.randomAlphabetic(10))
        payment.charge() // CHARGED 상태로 변경
        return payment
    }
}
