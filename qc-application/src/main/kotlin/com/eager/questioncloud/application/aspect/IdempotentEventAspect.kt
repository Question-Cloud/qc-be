package com.eager.questioncloud.application.aspect

import com.eager.questioncloud.application.event.SQSEvent
import com.eager.questioncloud.core.domain.event.infrastructure.repository.EventProcessLogRepository
import com.eager.questioncloud.core.domain.event.model.EventProcessLog
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
    @Around("@annotation(com.eager.questioncloud.application.event.IdempotentEvent)")
    fun processingEventIdempotency(joinPoint: ProceedingJoinPoint) {
        val event = joinPoint.args.first { it is SQSEvent } as SQSEvent
        val idempotentKey = event.eventId + "-" + joinPoint.signature.name

        if (eventProcessLogRepository.existsByIdempotentKey(idempotentKey)) {
            return
        }

        transactionTemplate.execute {
            joinPoint.proceed()
            eventProcessLogRepository.save(EventProcessLog.create(idempotentKey))
        }
    }
}