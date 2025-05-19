package com.eager

class CallTreeGenerator {
    companion object {
        fun generateCallTreeString(
            context: MethodRecordContext,
            indentUnit: String = "  "
        ): String {
            val sb = StringBuilder()

            for (record in context.methodRecords) {
                repeat(record.depth) {
                    sb.append(indentUnit)
                }

                if (record.depth > 0) {
                    sb.append("└─ ")
                } else {
                    sb.append("▶ ")
                }

                val fullMethodName = record.methodName
                val lastDotIndex = fullMethodName.lastIndexOf('.')
                val displayMethodName = if (lastDotIndex != -1 && lastDotIndex < fullMethodName.length - 1) {
                    val classNameStartIndex = fullMethodName.substring(0, lastDotIndex).lastIndexOf('.')
                    val simpleClassName = if (classNameStartIndex != -1 && classNameStartIndex < lastDotIndex) {
                        fullMethodName.substring(classNameStartIndex + 1, lastDotIndex)
                    } else {
                        fullMethodName.substring(0, lastDotIndex)
                    }
                    "$simpleClassName.${fullMethodName.substring(lastDotIndex + 1)}"
                } else {
                    fullMethodName
                }
                sb.append(displayMethodName)

                sb.append(" (").append(record.runningTime).append("ms)")

                if (record.exceptionMessage != null) {
                    sb.append(" [EXCEPTION!!]")
                }

                sb.append("\n")
            }

            return sb.toString()
        }
    }
}