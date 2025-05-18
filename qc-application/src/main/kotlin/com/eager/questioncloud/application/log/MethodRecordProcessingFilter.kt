package com.eager.questioncloud.application.log

import com.eager.MethodRecordContextHolder
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

@Component
class MethodRecordProcessingFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val logger = LoggerFactory.getLogger("method-record")
        val objectMapper: ObjectMapper = ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        runCatching {
            MethodRecordContextHolder.init()
            filterChain.doFilter(request, response)
        }.also {
            if (MethodRecordContextHolder.isActive()) {
                MethodRecordContextHolder.end()

                val context = MethodRecordContextHolder.get()
                logger.info(objectMapper.writeValueAsString(context))
                MethodRecordContextHolder.destroy()
            }
        }
    }
}