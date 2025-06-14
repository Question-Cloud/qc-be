package com.eager.questioncloud.event


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class IdempotentEvent
