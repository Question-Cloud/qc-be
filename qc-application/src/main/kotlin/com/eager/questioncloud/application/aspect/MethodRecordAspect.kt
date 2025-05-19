package com.eager.questioncloud.application.aspect

import com.eager.ApiTransactionContextHolder
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Component
@Aspect
class MethodRecordAspect {
    @Around("(@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Repository) || @within(org.springframework.stereotype.Service) || @within(org.springframework.stereotype.Component)) && execution(public * *(..)) && within(com.eager..*)")
    fun processMethodLog(joinPoint: ProceedingJoinPoint): Any? {
        if (!ApiTransactionContextHolder.isActive()) {
            return joinPoint.proceed()
        }

        val methodName = joinPoint.signature.declaringTypeName + "." + joinPoint.signature.name
        val methodRecordIndex = ApiTransactionContextHolder.startMethod(methodName)

        return runCatching { joinPoint.proceed() }
            .onSuccess {
                ApiTransactionContextHolder.endMethod(methodRecordIndex)
                return it
            }
            .onFailure {
                ApiTransactionContextHolder.markException(methodRecordIndex, it.stackTraceToString())
                throw it
            }
    }
}