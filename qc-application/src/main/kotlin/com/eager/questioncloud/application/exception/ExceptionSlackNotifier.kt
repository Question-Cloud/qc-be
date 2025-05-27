package com.eager.questioncloud.application.exception

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class ExceptionSlackNotifier {
    @Value("\${SLACK_EXCEPTION_NOTIFY_WEBHOOK_URL}")
    private lateinit var SLACK_EXCEPTION_NOTIFY_WEBHOOK_URL: String

    companion object {
        private val objectMapper =
            ObjectMapper().registerKotlinModule().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        private val client = OkHttpClient().newBuilder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    fun sendApiException(e: Exception, transactionId: String, url: String, method: String) {
        val payload = createPayload(e, transactionId, url, method)
        val request = Request.Builder()
            .url(SLACK_EXCEPTION_NOTIFY_WEBHOOK_URL)
            .post(objectMapper.writeValueAsString(payload).toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response -> println(response.body!!.string()) }
    }

    private fun createPayload(e: Exception, transactionId: String, url: String, method: String): SlackPayload {
        val builder = StringBuilder()
        builder.append("🚨 *서버 에러 발생* 🚨\n")
        builder.append("*Transaction ID:* `$transactionId`\n")
        builder.append("*URL:* `$url`\n")
        builder.append("*Method:* `$method`\n")
        builder.append("*Exception Name:* `${e::class.simpleName}`\n")
        builder.append("*Error Message:* ${e.message}\n\n")
        builder.append("*Stack Trace:*\n")
        builder.append("```${e.stackTraceToString().take(2000)}```")
        return SlackPayload(builder.toString())
    }
}

class SlackPayload(val text: String)