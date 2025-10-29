package com.eager.questioncloud.workspace.config

import com.eager.questioncloud.workspace.resolver.CreatorPrincipalArgumentResolver
import org.springframework.stereotype.Component
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class CreatorPrincipalResolverConfig(
    private val creatorPrincipalArgumentResolver: CreatorPrincipalArgumentResolver
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(creatorPrincipalArgumentResolver)
    }
}