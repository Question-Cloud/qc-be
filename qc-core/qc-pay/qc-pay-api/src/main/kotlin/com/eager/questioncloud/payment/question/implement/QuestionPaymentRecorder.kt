package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.repository.DiscountHistoryRepository
import com.eager.questioncloud.payment.repository.QuestionOrderRepository
import com.eager.questioncloud.payment.repository.QuestionPaymentRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class QuestionPaymentRecorder(
    private val questionPaymentRepository: QuestionPaymentRepository,
    private val questionOrderRepository: QuestionOrderRepository,
    private val discountHistoryRepository: DiscountHistoryRepository,
) {
    @Transactional
    fun record(questionPayment: QuestionPayment) {
        questionOrderRepository.save(questionPayment.order)
        questionPaymentRepository.save(questionPayment)
        discountHistoryRepository.saveAll(questionPayment.discountHistory)
    }
}