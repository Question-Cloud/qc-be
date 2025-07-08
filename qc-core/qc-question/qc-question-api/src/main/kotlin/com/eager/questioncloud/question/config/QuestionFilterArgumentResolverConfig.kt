package com.eager.questioncloud.question.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class QuestionFilterArgumentResolverConfig(
    private val questionFilterArgumentResolver: com.eager.questioncloud.question.resolver.QuestionFilterArgumentResolver,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(questionFilterArgumentResolver)
    }
}