package com.eager.questioncloud.test.resolver

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pagination.PagingInformation
import org.springframework.context.annotation.Profile
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
@Profile("test")
class TestPagingInformationArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == PagingInformation::class.java
    }
    
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): PagingInformation {
        val page = webRequest.getParameter("page")?.toIntOrNull()?.minus(1) ?: throw CoreException(Error.BAD_REQUEST)
        val size = webRequest.getParameter("size")?.toIntOrNull() ?: throw CoreException(Error.BAD_REQUEST)
        
        if (page < 0) {
            throw CoreException(Error.BAD_REQUEST)
        }
        
        return PagingInformation(page * size, size)
    }
}
