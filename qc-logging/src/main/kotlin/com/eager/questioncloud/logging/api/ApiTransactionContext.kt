package com.eager.questioncloud.logging.api

import org.slf4j.MDC

class ApiTransactionContext(
    val transactionId: String = MDC.get("traceId"),
    var apiRequest: ApiRequest? = null,
    var apiResponse: ApiResponse? = null,
) {
    fun loggingApiRequest(apiRequest: ApiRequest) {
        this.apiRequest = apiRequest
    }
    
    fun loggingApiResponse(apiResponse: ApiResponse) {
        this.apiResponse = apiResponse
    }
}