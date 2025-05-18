package com.eager

class MethodRecord(
    val methodName: String,
    val depth: Int,
    val startMethodTime: Long = System.currentTimeMillis(),
    var endMethodTime: Long = System.currentTimeMillis(),
    var runningTime: Long = 0,
    var isProcessed: Boolean = false,
    var exceptionMessage: String? = null
) {
    fun end() {
        endMethodTime = System.currentTimeMillis()
        runningTime = endMethodTime - startMethodTime
        isProcessed = true
    }

    fun markException(exceptionMessage: String?) {
        this.exceptionMessage = exceptionMessage
        endMethodTime = System.currentTimeMillis()
        runningTime = endMethodTime - startMethodTime
    }

    fun endByException() {
        endMethodTime = System.currentTimeMillis()
        runningTime = endMethodTime - startMethodTime
    }
}