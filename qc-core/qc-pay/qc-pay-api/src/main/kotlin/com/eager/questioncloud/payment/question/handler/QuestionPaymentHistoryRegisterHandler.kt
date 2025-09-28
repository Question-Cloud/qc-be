package com.eager.questioncloud.payment.question.handler

import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import com.eager.questioncloud.payment.domain.QuestionPaymentHistoryOrder
import com.eager.questioncloud.payment.domain.SimpleDiscountHistory
import com.eager.questioncloud.payment.repository.DiscountHistoryRepository
import com.eager.questioncloud.payment.repository.QuestionOrderRepository
import com.eager.questioncloud.payment.repository.QuestionPaymentHistoryRepository
import com.eager.questioncloud.payment.repository.QuestionPaymentRepository
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import org.springframework.stereotype.Component

@Component
class QuestionPaymentHistoryRegisterHandler(
    private val questionQueryAPI: QuestionQueryAPI,
    private val creatorQueryAPI: CreatorQueryAPI,
    private val userQueryAPI: UserQueryAPI,
    private val questionPaymentRepository: QuestionPaymentRepository,
    private val questionOrderRepository: QuestionOrderRepository,
    private val discountHistoryRepository: DiscountHistoryRepository,
    private val questionPaymentHistoryRepository: QuestionPaymentHistoryRepository,
) {
    fun saveQuestionPaymentHistory(event: QuestionPaymentEvent) {
        val questions = questionQueryAPI.getQuestionInformation(event.questionIds)
        val creators = creatorQueryAPI.getCreators(questions.map { it.creatorId })
        
        val questionMap = questions.associateBy { it.id }
        val creatorMap = creators.associateBy { it.creatorId }
        val creatorUserMap = userQueryAPI.getUsers(creators.map { it.userId }).associateBy { it.userId }
        
        val questionPaymentData = questionPaymentRepository.getQuestionPaymentData(event.orderId)
        val questionOrderData = questionOrderRepository.getQuestionOrderData(event.orderId)
        val discountHistories =
            discountHistoryRepository.findByOrderId(event.orderId).map { SimpleDiscountHistory(it.name, it.discountAmount) }
        
        val orders = questionOrderData.map {
            val question = questionMap.getValue(it.questionId)
            val creator = creatorMap.getValue(question.creatorId)
            val creatorUser = creatorUserMap.getValue(creator.userId)
            QuestionPaymentHistoryOrder(
                question.id,
                it.originalPrice,
                it.realPrice,
                it.promotionName,
                it.promotionDiscountAmount,
                question.title,
                question.thumbnail,
                creatorUser.name,
                question.subject,
                question.parentCategory,
                question.childCategory
            )
        }
        
        questionPaymentHistoryRepository.save(
            QuestionPaymentHistory.create(
                event.orderId,
                event.buyerUserId,
                orders,
                discountHistories,
                questionPaymentData.originalAmount,
                questionPaymentData.realAmount
            )
        )
    }
}
