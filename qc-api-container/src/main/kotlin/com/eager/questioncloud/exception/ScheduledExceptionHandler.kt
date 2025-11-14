package com.eager.questioncloud.exception

import io.sentry.IScope
import io.sentry.Sentry
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Component
@Aspect
class ScheduledExceptionHandler {
    @Around("execution(public * *(..)) && @annotation(org.springframework.scheduling.annotation.Scheduled) && !execution(* *(.., kotlin.coroutines.Continuation))")
    fun handleSchedulerException(joinPoint: ProceedingJoinPoint): Any? {
        Sentry.withScope { scope: IScope ->
            scope.setTransaction(joinPoint.signature.name)
            
            try {
                joinPoint.proceed()
            } catch (e: Throwable) {
                Sentry.captureException(e)
            }
        }
        
        return null
    }
}