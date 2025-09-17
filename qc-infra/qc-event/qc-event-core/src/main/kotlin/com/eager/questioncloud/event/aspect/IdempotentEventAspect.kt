package com.eager.questioncloud.event.aspect

import com.eager.questioncloud.common.event.Event
import com.eager.questioncloud.event.annotation.IdempotentEvent
import com.eager.questioncloud.event.model.EventProcessLog
import com.eager.questioncloud.event.repository.EventProcessLogRepository
import io.awspring.cloud.sqs.annotation.SqsListener
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate

@Component
@Aspect
class IdempotentEventAspect(
    private val eventProcessLogRepository: EventProcessLogRepository,
    private val transactionTemplate: TransactionTemplate,
) {
    @Around("@annotation(idempotentEvent) && @annotation(sqsListener)")
    fun processingEventIdempotency(joinPoint: ProceedingJoinPoint, idempotentEvent: IdempotentEvent, sqsListener: SqsListener) {
        val event = joinPoint.args.first { it is Event } as Event
        val idempotentKey = event.eventId + "-" + sqsListener.value[0]
        
        if (eventProcessLogRepository.existsByIdempotentKey(idempotentKey)) {
            return
        }
        
        transactionTemplate.execute {
            joinPoint.proceed()
            eventProcessLogRepository.save(EventProcessLog.create(idempotentKey))
        }
    }
}