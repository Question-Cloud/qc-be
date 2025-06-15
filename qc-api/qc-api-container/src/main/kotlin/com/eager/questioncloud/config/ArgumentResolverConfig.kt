package com.eager.questioncloud.config

import com.eager.questioncloud.resolver.PagingInformationArgumentResolver
import com.eager.questioncloud.resolver.QuestionFilterArgumentResolver
import com.eager.questioncloud.resolver.UserPrincipalArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ArgumentResolverConfig(
    private val pagingInformationArgumentResolver: PagingInformationArgumentResolver,
    private val questionFilterArgumentResolver: QuestionFilterArgumentResolver,
    private val userPrincipalArgumentResolver: UserPrincipalArgumentResolver
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(questionFilterArgumentResolver)
        resolvers.add(pagingInformationArgumentResolver)
        resolvers.add(userPrincipalArgumentResolver)
    }
}