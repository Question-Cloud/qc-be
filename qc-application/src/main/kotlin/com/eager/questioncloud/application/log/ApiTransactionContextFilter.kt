package com.eager.questioncloud.application.log

import com.eager.ApiRequest
import com.eager.ApiTransactionContextHolder
import com.eager.SensitiveBodyMasker
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import java.nio.charset.Charset

@Component
class ApiTransactionContextFilter : OncePerRequestFilter() {
    private val fileLogger = LoggerFactory.getLogger("api-transaction")
    private val objectMapper: ObjectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val cachingRequest = ContentCachingRequestWrapper(request)
        runCatching {
            ApiTransactionContextHolder.init()
            filterChain.doFilter(cachingRequest, response)
        }.also {
            ApiTransactionContextHolder.loggingApiRequest(toApiRequest(cachingRequest))
            ApiTransactionContextHolder.end()

            val apiTransactionContext = ApiTransactionContextHolder.get()
            fileLogger.info(objectMapper.writeValueAsString(apiTransactionContext))

            ApiTransactionContextHolder.destroy()
        }.getOrThrow()
    }

    private fun toApiRequest(request: ContentCachingRequestWrapper): ApiRequest {
        return ApiRequest(
            request.requestURI,
            request.method,
            SensitiveBodyMasker.mask(parseBody(request)),
            request.remoteAddr,
            parseHeaders(request),
            request.parameterMap
        )
    }

    private fun parseHeaders(request: ContentCachingRequestWrapper): Map<String, String> {
        val headers = HashMap<String, String>()

        for (key in request.headerNames) {
            headers[key] = request.getHeader(key)
        }

        return headers
    }

    private fun parseBody(request: ContentCachingRequestWrapper): String {
        if (request.contentAsString.isNotEmpty()) {
            return request.contentAsString
        }

        val inputStream = request.inputStream.readAllBytes()

        if (inputStream.isEmpty()) {
            return ""
        }

        return inputStream.toString(Charset.forName("UTF-8"))
    }
}