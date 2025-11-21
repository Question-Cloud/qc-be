package com.eager.questioncloud.common.event

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class QueueListener(
    val queueName: String,
    val type: KClass<out Event>
)
