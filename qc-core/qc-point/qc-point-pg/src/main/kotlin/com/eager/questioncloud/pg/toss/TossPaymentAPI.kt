package com.eager.questioncloud.pg.toss

import com.eager.questioncloud.common.http.ContentType
import com.eager.questioncloud.common.http.HttpClient
import com.eager.questioncloud.common.http.HttpClientException
import com.eager.questioncloud.common.http.HttpRequest
import com.eager.questioncloud.common.pg.*
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
        return PGPayment(response.paymentKey, response.orderId, response.totalAmount, PGPaymentStatus.valueOf(response.status.name))
    }
    
    override fun confirm(pgConfirmRequest: PGConfirmRequest): PGConfirmResult {
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "Basic $TOSS_SECRET_KEY"
        headers["Idempotency-Key"] = pgConfirmRequest.paymentId
        
        val request = HttpRequest(
            url = "${BASE_URL}/payments/confirm",
            headers = headers,
            body = TossPaymentConfirmRequest(pgConfirmRequest.paymentId, pgConfirmRequest.orderId, pgConfirmRequest.amount),
            contentType = ContentType.JSON,
        )
        
        try {
            val response = httpClient.post(request, TossPayment::class.java)
            return PGConfirmResult.Success(response.orderId, response.paymentKey)
        } catch (e: Exception) {
            if (e is HttpClientException.Response4xxException) {
                return PGConfirmResult.Fail(pgConfirmRequest.orderId, pgConfirmRequest.paymentId)
            }
            throw e
        }
    }
    
    override fun cancel(pgConfirmRequest: PGConfirmRequest) {
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "Basic $TOSS_SECRET_KEY"
        headers["Idempotency-Key"] = pgConfirmRequest.paymentId
        
        val request = HttpRequest(
            url = "${BASE_URL}/payments/${pgConfirmRequest.paymentId}/cancel",
            headers = headers,
            body = TossPaymentCancelRequest(pgConfirmRequest.amount),
            contentType = ContentType.JSON,
        )
        
        httpClient.post(request)
    }
    
    companion object {
        private const val BASE_URL = "https://api.tosspayments.com/v1"
    }
}