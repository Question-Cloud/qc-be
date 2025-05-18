package com.eager

class MethodRecordContextHolder {
    companion object {
        private val methodRecordContext: ThreadLocal<MethodRecordContext> = ThreadLocal()
        private val curDepth: ThreadLocal<Int> = ThreadLocal()

        fun init() {
            methodRecordContext.set(MethodRecordContext())
            curDepth.set(0)
        }

        fun startMethod(methodName: String): Int {
            val context = methodRecordContext.get()
            val newRecordIndex = context.startMethod(methodName, curDepth.get())
            curDepth.set(curDepth.get().inc())
            return newRecordIndex
        }

        fun endMethod(targetMethodRecordIndex: Int) {
            val context = methodRecordContext.get()
            context.endMethod(targetMethodRecordIndex)
            curDepth.set(curDepth.get().dec())
        }

        fun end() {
            val context = methodRecordContext.get()
            context.end()
        }

        fun markException(targetMethodRecordIndex: Int, exceptionMessage: String?) {
            val context = methodRecordContext.get()
            context.markException(targetMethodRecordIndex, exceptionMessage)
        }

        fun destroy() {
            methodRecordContext.remove()
            curDepth.remove()
        }

        fun get(): MethodRecordContext {
            return methodRecordContext.get()
        }

        fun isActive(): Boolean {
            return methodRecordContext.get() != null
        }
    }
}