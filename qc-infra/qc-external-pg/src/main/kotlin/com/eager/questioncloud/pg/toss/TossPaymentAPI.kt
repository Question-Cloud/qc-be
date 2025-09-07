package com.eager.questioncloud.pg.toss

import com.eager.questioncloud.http.ContentType
import com.eager.questioncloud.http.HttpClient
import com.eager.questioncloud.http.HttpRequest
import com.eager.questioncloud.pg.PaymentAPI
import com.eager.questioncloud.pg.model.PGPayment
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TossPaymentAPI(
    private val httpClient: HttpClient,
) : PaymentAPI {
    @Value("\${TOSS_SECRET_KEY}")
    private lateinit var TOSS_SECRET_KEY: String
    
    override fun getPayment(orderId: String): PGPayment {
        val headers = mapOf("Authorization" to "Basic $TOSS_SECRET_KEY")
        val request = HttpRequest(url = "${BASE_URL}/payments/orders/$orderId", headers = headers)
        val response = httpClient.get(request, TossPayment::class.java)
        return PGPayment(response.paymentKey, response.orderId, response.totalAmount, response.status)
    }
    
    override fun confirm(pgPayment: PGPayment) {
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "Basic $TOSS_SECRET_KEY"
        headers["Idempotency-Key"] = pgPayment.paymentId
        
        val request = HttpRequest(
            url = "${BASE_URL}/payments/confirm",
            headers = headers,
            body = TossPaymentConfirmRequest(pgPayment.paymentId, pgPayment.orderId, pgPayment.amount),
            contentType = ContentType.JSON,
        )
        
        httpClient.post(request)
    }
    
    override fun cancel(pgPayment: PGPayment) {
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "Basic $TOSS_SECRET_KEY"
        headers["Idempotency-Key"] = pgPayment.paymentId
        
        val request = HttpRequest(
            url = "${BASE_URL}/payments/${pgPayment.paymentId}/cancel",
            headers = headers,
            body = TossPaymentCancelRequest(pgPayment.amount),
            contentType = ContentType.JSON,
        )
        
        httpClient.post(request)
    }
    
    companion object {
        private const val BASE_URL = "https://api.tosspayments.com/v1"
    }
}