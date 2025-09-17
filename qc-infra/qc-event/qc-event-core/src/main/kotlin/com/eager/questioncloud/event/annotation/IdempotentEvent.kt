package com.eager.questioncloud.event.annotation


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class IdempotentEvent
