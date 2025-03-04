package com.eager.questioncloud.pg.portone

import com.eager.questioncloud.pg.exception.InvalidPaymentIdException
import com.eager.questioncloud.pg.exception.PGException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class PortoneAPI {
    @Value("\${PORT_ONE_SECRET_KEY}")
    private lateinit var PORT_ONE_SECRET_KEY: String

    fun getPayment(paymentId: String): PortonePayment {
        val request = Request.Builder()
            .url("$BASE_URL/payments/$paymentId")
            .header("Authorization", "Portone $PORT_ONE_SECRET_KEY")
            .get()
            .build()
        try {
            client.newCall(request).execute().use { response ->
                validatePaymentResponse(response)
                return objectMapper.readValue(response.body!!.string(), PortonePayment::class.java)
            }
        } catch (e: InvalidPaymentIdException) {
            throw e
        } catch (e: Exception) {
            println(e.message)
            throw PGException()
        }
    }

    fun cancel(paymentId: String?) {
        val request = Request.Builder()
            .url("$BASE_URL/payments/$paymentId/cancel")
            .header("Authorization", "Portone $PORT_ONE_SECRET_KEY")
            .post(getCancelRequestBody())
            .build()

        try {
            client.newCall(request).execute().use { response ->
                validateCancelResponse(response)
            }
        } catch (e: Exception) {
            throw PGException()
        }
    }

    private fun getCancelRequestBody(): RequestBody {
        try {
            return objectMapper.writeValueAsString(PortoneCancelRequest("Invalid Payment")).toRequestBody()
        } catch (e: Exception) {
            throw PGException()
        }
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

    private fun validateCancelResponse(response: Response) {
        if (response.code == 200) {
            return
        }
        throw PGException()
    }

    companion object {
        private const val BASE_URL = "https://api.portone.io"
        private val objectMapper = ObjectMapper().registerKotlinModule()
        private val client = OkHttpClient().newBuilder()
            .callTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }
}