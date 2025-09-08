package com.eager.questioncloud.payment.question.listener

import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.event.model.QuestionPaymentEvent
import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import com.eager.questioncloud.payment.domain.QuestionPaymentHistoryOrder
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.repository.QuestionPaymentHistoryRepository
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class QuestionPaymentHistoryRegisterListener(
    private val questionQueryAPI: QuestionQueryAPI,
    private val creatorQueryAPI: CreatorQueryAPI,
    private val userQueryAPI: UserQueryAPI,
    private val questionPaymentHistoryRepository: QuestionPaymentHistoryRepository,
) {
    @SqsListener("question-payment-history-register.fifo")
    fun saveQuestionPaymentHistory(@Payload event: QuestionPaymentEvent) {
        val questions = questionQueryAPI.getQuestionInformation(event.data.questionIds)
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
        
        val couponData = event.data.questionPaymentCoupon
        
        questionPaymentHistoryRepository.save(
            QuestionPaymentHistory.create(
                event.data.orderId,
                event.data.buyerUserId,
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
                event.data.amount
            )
        )
    }
}
