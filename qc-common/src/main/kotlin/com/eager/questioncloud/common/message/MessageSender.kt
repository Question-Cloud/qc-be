package com.eager.questioncloud.common.message

interface MessageSender {
    fun sendMessage(payload: Any, to: String, key: String)
}