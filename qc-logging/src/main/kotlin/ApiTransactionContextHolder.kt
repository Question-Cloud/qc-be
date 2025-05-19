package com.eager

class ApiTransactionContextHolder {
    companion object {
        private val apiTransactionContext: ThreadLocal<ApiTransactionContext> = ThreadLocal()

        fun init() {
            apiTransactionContext.set(ApiTransactionContext())
        }

        fun loggingApiRequest(apiRequest: ApiRequest) {
            val context = apiTransactionContext.get()
            context.loggingApiRequest(apiRequest)
        }

        fun startMethod(methodName: String): Int {
            val context = apiTransactionContext.get()
            return context.startMethod(methodName)
        }

        fun endMethod(targetMethodRecordIndex: Int) {
            val context = apiTransactionContext.get()
            context.endMethod(targetMethodRecordIndex)
        }

        fun end() {
            val context = apiTransactionContext.get()
            context.end()
        }

        fun markException(targetMethodRecordIndex: Int, exceptionMessage: String?) {
            val context = apiTransactionContext.get()
            context.markException(targetMethodRecordIndex, exceptionMessage)
        }

        fun destroy() {
            apiTransactionContext.remove()
        }

        fun get(): ApiTransactionContext {
            return apiTransactionContext.get()
        }

        fun isActive(): Boolean {
            return apiTransactionContext.get() != null
        }
    }
}