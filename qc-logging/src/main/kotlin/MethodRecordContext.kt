package com.eager

class MethodRecordContext(
    val methodRecords: MutableList<MethodRecord> = mutableListOf(),
    var isOccurredException: Boolean = false,
    var exceptionRecordIndex: Int? = null,
    var exceptionMessage: String? = null,
    var callTree: String? = null,
    var curDepth: Int = 0,
) {
    fun startMethod(methodName: String): Int {
        methodRecords.add(MethodRecord(methodName, curDepth++))
        return methodRecords.size - 1
    }

    fun endMethod(target: Int) {
        val methodRecord = methodRecords[target]
        methodRecord.end()
        curDepth--
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