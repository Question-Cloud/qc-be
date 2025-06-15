package com.eager.questioncloud.filter

import com.eager.ApiTransactionContextHolder
import com.eager.questioncloud.common.exception.ExceptionSlackNotifier
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
class FilterExceptionHandlerFilter(
    private val exceptionSlackNotifier: ExceptionSlackNotifier
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        runCatching { filterChain.doFilter(request, response) }
            .onFailure { e ->
                if (ApiTransactionContextHolder.isMarkedException()) {
                    return@onFailure
                }
                exceptionSlackNotifier.sendApiException(
                    e,
                    ApiTransactionContextHolder.get().transactionId,
                    request.requestURI,
                    request.method
                )
            }
    }
}