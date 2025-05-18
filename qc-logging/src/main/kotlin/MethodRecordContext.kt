package com.eager

import java.util.*

class MethodRecordContext(
    val requestId: String = UUID.randomUUID().toString(),
    val methodRecords: MutableList<MethodRecord> = mutableListOf(),
    var isOccurredException: Boolean = false,
    var exceptionRecordIndex: Int? = null,
    var exceptionMessage: String? = null,
    var callTree: String? = null
) {
    fun startMethod(methodName: String, depth: Int): Int {
        methodRecords.add(MethodRecord(methodName = methodName, depth = depth))
        return methodRecords.size - 1
    }

    fun endMethod(target: Int) {
        val methodRecord = methodRecords[target]
        methodRecord.end()
    }

    fun end() {
        callTree = CallTreeGenerator.generateCallTreeString(this)
    }

    fun markException(targetMethodRecordIndex: Int, exceptionMessage: String?) {
        val methodRecord = methodRecords[targetMethodRecordIndex]

        if (isOccurredException) {
            methodRecord.endByException()
            return
        }

        methodRecord.markException(exceptionMessage)
        isOccurredException = true
        exceptionRecordIndex = targetMethodRecordIndex
        this.exceptionMessage = exceptionMessage
    }
}