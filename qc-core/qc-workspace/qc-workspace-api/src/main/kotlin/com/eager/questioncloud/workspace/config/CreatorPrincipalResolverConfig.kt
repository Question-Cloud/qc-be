package com.eager.questioncloud.workspace.config

import com.eager.questioncloud.workspace.resolver.CreatorPrincipalArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@Profile("prod", "local")
class CreatorPrincipalResolverConfig(
    private val creatorPrincipalArgumentResolver: CreatorPrincipalArgumentResolver
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(creatorPrincipalArgumentResolver)
    }
}