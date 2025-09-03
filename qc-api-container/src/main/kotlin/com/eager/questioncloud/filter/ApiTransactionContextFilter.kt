package com.eager.questioncloud.filter

import com.eager.questioncloud.logging.api.ApiRequest
import com.eager.questioncloud.logging.api.ApiResponse
import com.eager.questioncloud.logging.api.ApiTransactionContextHolder
import com.eager.questioncloud.logging.api.SensitiveBodyMasker
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.nio.charset.Charset

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
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
        val cachingResponse = ContentCachingResponseWrapper(response)
        
        runCatching {
            ApiTransactionContextHolder.init()
            filterChain.doFilter(cachingRequest, cachingResponse)
        }.onFailure {
            toErrorResponse(cachingResponse)
        }.also {
            ApiTransactionContextHolder.end()
            ApiTransactionContextHolder.loggingApiRequest(toApiRequest(cachingRequest))
            ApiTransactionContextHolder.loggingApiResponse(toApiResponse(cachingResponse))
            fileLogger.info(objectMapper.writeValueAsString(ApiTransactionContextHolder.get()))
            
            ApiTransactionContextHolder.destroy()
            
            cachingResponse.copyBodyToResponse()
        }
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
    
    private fun toApiResponse(response: ContentCachingResponseWrapper): ApiResponse {
        return ApiResponse(
            response.status,
            SensitiveBodyMasker.mask(response.contentInputStream.readAllBytes().toString(Charset.forName("UTF-8"))),
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
    
    private fun toErrorResponse(response: ContentCachingResponseWrapper) {
        response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        response.setHeader("Content-Type", "application/json")
    }
}