package com.eager.questioncloud.application.exception

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
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

    fun send(message: String) {
        val slackPayload = SlackPayload(message)
        val request = Request.Builder()
            .url(SLACK_EXCEPTION_NOTIFY_WEBHOOK_URL)
            .post(toRequest(slackPayload))
            .build()

        client.newCall(request).execute()
    }

    fun toRequest(payload: SlackPayload): RequestBody {
        return objectMapper.writeValueAsString(payload).toRequestBody("application/json".toMediaType())
    }
}

class SlackPayload(
    val text: String,
)