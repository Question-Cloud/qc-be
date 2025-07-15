package com.eager.questioncloud.exception

import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.exception.ExceptionSlackNotifier
import com.eager.questioncloud.logging.ApiTransactionContextHolder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
class NonCoreExceptionResolver(private val slackNotifier: ExceptionSlackNotifier) : HandlerExceptionResolver {
    private val cacheResponse =
        ErrorResponse(Error.INTERNAL_SERVER_ERROR.httpStatus, Error.INTERNAL_SERVER_ERROR.message)
    private val objectMapper = ObjectMapper().registerKotlinModule()
    
    override fun resolveException(
        req: HttpServletRequest,
        res: HttpServletResponse,
        handler: Any?,
        ex: Exception
    ): ModelAndView {
        val transactionId = ApiTransactionContextHolder.get().transactionId
        slackNotifier.sendApiException(ex, transactionId, req.requestURI, req.method)
        ApiTransactionContextHolder.markException()
        writeResponse(res)
        return ModelAndView()
    }
    
    private fun writeResponse(response: HttpServletResponse) {
        response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.writer.write(objectMapper.writeValueAsString(cacheResponse))
    }
}