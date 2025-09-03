package com.eager.questioncloud.logging.trace

import com.eager.questioncloud.logging.api.ApiTransactionContextHolder
import io.micrometer.tracing.Tracer
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class TracingAspect(
    private val tracer: Tracer
) {
    @Around("(@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Repository) || @within(org.springframework.stereotype.Service) || @within(org.springframework.stereotype.Component)) && execution(public * *(..)) && within(com.eager..*)")
    fun processMethodLog(joinPoint: ProceedingJoinPoint): Any? {
        if (!ApiTransactionContextHolder.isActive()) {
            return joinPoint.proceed()
        }
        
        val methodName = "${joinPoint.signature.declaringTypeName}.${joinPoint.signature.name}"
        val span = tracer.nextSpan().name(methodName)
        span.start()
        return runCatching { tracer.withSpan(span).use { joinPoint.proceed() } }
            .onFailure { e -> span.error(e) }
            .also { span.end() }
            .getOrThrow()
    }
}