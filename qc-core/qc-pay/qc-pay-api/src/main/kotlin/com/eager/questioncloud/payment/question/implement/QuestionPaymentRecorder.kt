package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.domain.DiscountHistory
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.repository.DiscountHistoryRepository
import com.eager.questioncloud.payment.repository.QuestionOrderRepository
import com.eager.questioncloud.payment.repository.QuestionPaymentRepository
import org.springframework.stereotype.Component

@Component
class QuestionPaymentRecorder(
    private val questionPaymentRepository: QuestionPaymentRepository,
    private val questionOrderRepository: QuestionOrderRepository,
    private val discountHistoryRepository: DiscountHistoryRepository,
) {
    fun record(questionPayment: QuestionPayment) {
        questionPaymentRepository.save(questionPayment)
        questionOrderRepository.save(questionPayment.order)
        
        val discountHistories = mutableListOf<DiscountHistory>()
        discountHistories.addAll(questionPayment.paymentDiscount)
        discountHistories.addAll(questionPayment.orderDiscount)
        discountHistories.forEach { it.updatePaymentId(questionPayment.paymentId) }
        discountHistoryRepository.saveAll(discountHistories)
    }
}