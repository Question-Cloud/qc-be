package com.eager.questioncloud.workspace.resolver

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.workspace.auth.CreatorPrincipal
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class CreatorPrincipalArgumentResolver(
    private val creatorQueryAPI: CreatorQueryAPI
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == CreatorPrincipal::class.java
    }
    
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): CreatorPrincipal {
        val userPrincipal = SecurityContextHolder.getContext().authentication.principal as UserPrincipal
        
        if (userPrincipal.userId == -1L) throw CoreException(Error.FORBIDDEN)
        
        val creatorQueryData = creatorQueryAPI.getCreatorByUserId(userPrincipal.userId)
        
        return CreatorPrincipal(userPrincipal.userId, creatorQueryData.creatorId)
    }
}