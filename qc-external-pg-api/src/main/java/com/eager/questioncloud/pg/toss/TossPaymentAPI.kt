package com.eager.questioncloud.pg.toss

import com.eager.questioncloud.pg.exception.InvalidPaymentIdException
import com.eager.questioncloud.pg.exception.PGException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class TossPaymentAPI {
    @Value("\${TOSS_SECRET_KEY}")
    private lateinit var TOSS_SECRET_KEY: String

    fun getPayment(orderId: String): TossPayment {
        val request = Request.Builder()
            .url("${BASE_URL}/payments/orders/$orderId")
            .header("Authorization", "Basic $TOSS_SECRET_KEY")
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            validatePaymentResponse(response)
            return objectMapper.readValue(response.body!!.string(), TossPayment::class.java)
        }
    }

    fun confirm(paymentId: String, orderId: String, amount: Int) {
        val request = Request.Builder()
            .url("${BASE_URL}/payments/confirm")
            .header("Authorization", "Basic $TOSS_SECRET_KEY")
            .post(getConfirmRequestBody(TossPaymentConfirmRequest(paymentId, orderId, amount)))
            .build()

        client.newCall(request).execute().use { response ->
            validatePaymentResponse(response)
        }
    }

    fun cancel(paymentId: String, amount: Int) {
        val request = Request.Builder()
            .url("${BASE_URL}/payments/$paymentId/cancel")
            .header("Authorization", "Basic $TOSS_SECRET_KEY")
            .header("Idempotency-Key", paymentId)
            .post(getCancelRequestBody(TossPaymentCancelRequest(amount)))
            .build()

        client.newCall(request).execute().use { response ->
            validatePaymentResponse(response)
        }
    }

    private fun getConfirmRequestBody(request: TossPaymentConfirmRequest): RequestBody {
        return objectMapper.writeValueAsString(request).toRequestBody("application/json".toMediaType())
    }

    private fun getCancelRequestBody(request: TossPaymentCancelRequest): RequestBody {
        return objectMapper.writeValueAsString(request).toRequestBody("application/json".toMediaType())
    }

    private fun validatePaymentResponse(response: Response) {
        if (response.code == 200) {
            return
        }

        if (response.code == 404) {
            throw InvalidPaymentIdException()
        }

        if (response.body == null) {
            throw PGException()
        }
    }

    companion object {
        private const val BASE_URL = "https://api.tosspayments.com/v1"
        private val objectMapper = ObjectMapper().registerKotlinModule()
        private val client = OkHttpClient().newBuilder()
            .callTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }
}