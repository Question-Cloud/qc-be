package com.eager.questioncloud.common.event

interface MessageListener<T : Event> {
    fun onMessage(event: T)
}