package com.eager.questioncloud.test.config

import com.eager.questioncloud.test.resolver.TestCreatorPrincipalArgumentResolver
import com.eager.questioncloud.test.resolver.TestPagingInformationArgumentResolver
import com.eager.questioncloud.test.resolver.TestUserPrincipalArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@Profile("test")
class TestArgumentResolverConfig(
    private val testPagingInformationArgumentResolver: TestPagingInformationArgumentResolver,
    private val testUserPrincipalArgumentResolver: TestUserPrincipalArgumentResolver,
    private val testCreatorPrincipalArgumentResolver: TestCreatorPrincipalArgumentResolver
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(testPagingInformationArgumentResolver)
        resolvers.add(testUserPrincipalArgumentResolver)
        resolvers.add(testCreatorPrincipalArgumentResolver)
    }
}