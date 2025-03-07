package com.eager.questioncloud.application.business.payment.question.implement

import com.eager.questioncloud.application.message.FailQuestionPaymentMessage
import com.eager.questioncloud.application.message.MessageSender
import com.eager.questioncloud.application.message.MessageType
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentRepository
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FailQuestionPaymentMessageListener(
    private val questionPaymentRepository: QuestionPaymentRepository,
    private val userPointRepository: UserPointRepository,
    private val userCouponRepository: UserCouponRepository,
    private val messageSender: MessageSender,
) {
    @RabbitListener(id = "fail.question.payment", queues = ["fail.question.payment"])
    @Transactional
    fun handler(message: FailQuestionPaymentMessage) {
        try {
            val questionPayment = message.questionPayment
            questionPayment.fail()
            questionPaymentRepository.save(questionPayment)

            rollbackPoint(questionPayment.userId, questionPayment.amount)
            questionPayment.questionPaymentCoupon?.let { rollbackCoupon(it.userCouponId) }
        } catch (e: Exception) {
            message.increaseFailCount()
            messageSender.sendDelayMessage(MessageType.FAIL_QUESTION_PAYMENT, message, message.failCount)
            throw AmqpRejectAndDontRequeueException(e)
        }
    }

    private fun rollbackPoint(userId: Long, amount: Int) {
        val userPoint = userPointRepository.getUserPoint(userId)
        userPoint.charge(amount)
        userPointRepository.save(userPoint)
    }

    private fun rollbackCoupon(userCouponId: Long) {
        val userCoupon = userCouponRepository.getUserCoupon(userCouponId)
        userCoupon.rollback()
        userCouponRepository.save(userCoupon)
    }
}
