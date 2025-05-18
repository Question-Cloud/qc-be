package com.eager.questioncloud.application.config

import com.eager.questioncloud.application.resolver.PagingInformationArgumentResolver
import com.eager.questioncloud.application.resolver.QuestionFilterArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val questionFilterArgumentResolver: QuestionFilterArgumentResolver,
    private val pagingInformationArgumentResolver: PagingInformationArgumentResolver,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(questionFilterArgumentResolver)
        resolvers.add(pagingInformationArgumentResolver)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedMethods(
                HttpMethod.GET.name(),
                HttpMethod.HEAD.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.OPTIONS.name()
            )
            .allowedOrigins("*")
    }
}
