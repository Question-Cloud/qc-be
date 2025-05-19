package com.eager.questioncloud.application.log

import com.eager.ApiTransactionContextHolder
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

@Component
class MethodRecordProcessingFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val logger = LoggerFactory.getLogger("api-transaction")
        val objectMapper: ObjectMapper = ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val cachingRequestWrapper = ContentCachingRequestWrapper(request)
        runCatching {
            ApiTransactionContextHolder.init()
            filterChain.doFilter(cachingRequestWrapper, response)
        }.also {
            if (ApiTransactionContextHolder.isActive()) {
                ApiTransactionContextHolder.end()

                val context = ApiTransactionContextHolder.get()
                logger.info(objectMapper.writeValueAsString(context))

                ApiTransactionContextHolder.destroy()
            }
        }
    }
}