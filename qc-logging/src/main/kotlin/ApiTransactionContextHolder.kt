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

        fun loggingApiResponse(apiResponse: ApiResponse) {
            val context = apiTransactionContext.get()
            context.loggingApiResponse(apiResponse)
        }

        fun end() {
            val context = apiTransactionContext.get()
            context.end()
        }

        fun markException() {
            val context = apiTransactionContext.get()
            context.markException()
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