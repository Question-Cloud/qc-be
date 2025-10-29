package com.eager.questioncloud.workspace.config

import com.eager.questioncloud.workspace.resolver.TestCreatorPrincipalArgumentResolver
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@TestConfiguration
class TestConfig(
    private val testCreatorPrincipalArgumentResolver: TestCreatorPrincipalArgumentResolver
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(testCreatorPrincipalArgumentResolver)
    }
}