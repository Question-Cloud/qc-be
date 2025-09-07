package com.eager.questioncloud.pg.toss

import com.eager.questioncloud.http.ContentType
import com.eager.questioncloud.http.HttpClient
import com.eager.questioncloud.http.HttpRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TossPaymentAPI(
    private val httpClient: HttpClient,
) {
    @Value("\${TOSS_SECRET_KEY}")
    private lateinit var TOSS_SECRET_KEY: String
    
    fun getPayment(orderId: String): TossPayment {
        val headers = mapOf("Authorization" to "Basic $TOSS_SECRET_KEY")
        val request = HttpRequest(url = "${BASE_URL}/payments/orders/$orderId", headers = headers)
        
        return httpClient.get(request, TossPayment::class.java)
    }
    
    fun confirm(paymentId: String, orderId: String, amount: Int) {
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "Basic $TOSS_SECRET_KEY"
        headers["Idempotency-Key"] = paymentId
        
        val request = HttpRequest(
            url = "${BASE_URL}/payments/confirm",
            headers = headers,
            body = TossPaymentConfirmRequest(paymentId, orderId, amount),
            contentType = ContentType.JSON,
        )
        
        httpClient.post(request)
    }
    
    fun cancel(paymentId: String, amount: Int) {
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "Basic $TOSS_SECRET_KEY"
        headers["Idempotency-Key"] = paymentId
        
        val request = HttpRequest(
            url = "${BASE_URL}/payments/$paymentId/cancel",
            headers = headers,
            body = TossPaymentCancelRequest(amount),
            contentType = ContentType.JSON,
        )
        
        httpClient.post(request)
    }
    
    companion object {
        private const val BASE_URL = "https://api.tosspayments.com/v1"
    }
}