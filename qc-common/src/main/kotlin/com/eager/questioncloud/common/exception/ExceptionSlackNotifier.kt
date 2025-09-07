package com.eager.questioncloud.common.exception

import com.eager.questioncloud.http.ContentType
import com.eager.questioncloud.http.HttpClient
import com.eager.questioncloud.http.HttpRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ExceptionSlackNotifier(
    private val httpClient: HttpClient,
) {
    @Value("\${SLACK_EXCEPTION_NOTIFY_WEBHOOK_URL}")
    private lateinit var SLACK_EXCEPTION_NOTIFY_WEBHOOK_URL: String
    
    fun sendApiException(e: Throwable, transactionId: String, url: String, method: String) {
        val payload = createPayload(e, transactionId, url, method)
        val request = HttpRequest(url = SLACK_EXCEPTION_NOTIFY_WEBHOOK_URL, body = payload, contentType = ContentType.JSON)
        httpClient.post(request)
    }
    
    private fun createPayload(e: Throwable, transactionId: String, url: String, method: String): SlackPayload {
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