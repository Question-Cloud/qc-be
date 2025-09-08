package com.eager.questioncloud.payment.question.handler

import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import com.eager.questioncloud.payment.domain.QuestionPaymentHistoryOrder
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.repository.QuestionPaymentHistoryRepository
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import org.springframework.stereotype.Component

@Component
class QuestionPaymentHistoryRegisterHandler(
    private val questionQueryAPI: QuestionQueryAPI,
    private val creatorQueryAPI: CreatorQueryAPI,
    private val userQueryAPI: UserQueryAPI,
    private val questionPaymentHistoryRepository: QuestionPaymentHistoryRepository,
) {
    fun saveQuestionPaymentHistory(event: QuestionPaymentEvent) {
        val questions = questionQueryAPI.getQuestionInformation(event.questionIds)
        val creators = creatorQueryAPI.getCreators(questions.map { it.creatorId })
        val creatorMap = creators.associateBy { it.creatorId }
        val creatorUserMap = userQueryAPI.getUsers(creators.map { it.userId }).associateBy { it.userId }
        
        val orders = questions.map {
            val creator = creatorMap.getValue(it.creatorId)
            val creatorUser = creatorUserMap.getValue(creator.userId)
            QuestionPaymentHistoryOrder(
                it.id,
                it.price,
                it.title,
                it.thumbnail,
                creatorUser.name,
                it.subject,
                it.parentCategory,
                it.childCategory
            )
        }
        
        val couponData = event.questionPaymentCoupon
        
        questionPaymentHistoryRepository.save(
            QuestionPaymentHistory.create(
                event.orderId,
                event.buyerUserId,
                orders,
                couponData?.let {
                    QuestionPaymentCoupon(
                        couponData.userCouponId,
                        couponData.couponId,
                        couponData.title,
                        CouponType.valueOf(couponData.couponType),
                        couponData.value
                    )
                },
                event.amount
            )
        )
    }
}
