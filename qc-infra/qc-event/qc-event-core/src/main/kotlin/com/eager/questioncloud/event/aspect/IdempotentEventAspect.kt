package com.eager.questioncloud.event.aspect

import com.eager.questioncloud.common.event.Event
import com.eager.questioncloud.common.event.QueueListener
import com.eager.questioncloud.event.model.EventProcessLog
import com.eager.questioncloud.event.repository.EventProcessLogRepository
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.aop.support.AopUtils
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate

@Component
@Aspect
class IdempotentEventAspect(
    private val eventProcessLogRepository: EventProcessLogRepository,
    private val transactionTemplate: TransactionTemplate,
) {
    @Around("@annotation(com.eager.questioncloud.common.event.IdempotentEvent)")
    fun processingEventIdempotency(joinPoint: ProceedingJoinPoint) {
        val event = joinPoint.args.first { it is Event } as Event
        val targetClass = AopUtils.getTargetClass(joinPoint.target)
        val annotation = AnnotationUtils.findAnnotation(targetClass, QueueListener::class.java) ?: return
        
        val idempotentKey = event.eventId + "-" + annotation.queueName
        
        if (eventProcessLogRepository.existsByIdempotentKey(idempotentKey)) {
            return
        }
        
        transactionTemplate.execute {
            joinPoint.proceed()
            eventProcessLogRepository.save(EventProcessLog.create(idempotentKey))
        }
    }
}