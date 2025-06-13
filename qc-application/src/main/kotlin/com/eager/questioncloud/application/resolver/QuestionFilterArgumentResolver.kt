package com.eager.questioncloud.application.resolver

import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import com.eager.questioncloud.core.domain.question.enums.QuestionLevel
import com.eager.questioncloud.core.domain.question.enums.QuestionSortType
import com.eager.questioncloud.core.domain.question.enums.QuestionType
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.*
import java.util.stream.Collectors

@Component
class QuestionFilterArgumentResolver(
    private val pagingInformationArgumentResolver: PagingInformationArgumentResolver
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == QuestionFilter::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): QuestionFilter {
        val userId = userIdFromRequest()
        val categories = getCategoriesFromRequest(webRequest)
        val levels = getLevelsFromRequest(webRequest)
        val questionType = getQuestionTypeFromRequest(webRequest)
        val creatorId = getCreatorIdFromRequest(webRequest)
        val sort = getSortFromRequest(webRequest)
        val pagingInformation =
            pagingInformationArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory)
        return QuestionFilter(userId, categories, levels, questionType, creatorId, sort!!, pagingInformation)
    }

    private fun userIdFromRequest(): Long {
        val userPrincipal = SecurityContextHolder.getContext().authentication.principal as UserPrincipal
        return userPrincipal.user.uid
    }
    
    private fun getCategoriesFromRequest(webRequest: NativeWebRequest): List<Long>? {
        val input = webRequest.getParameter("categories") ?: return null

        val categoriesParams = input.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        return Arrays.stream(categoriesParams)
            .map { s: String -> s.toLong() }
            .collect(Collectors.toList())
    }

    private fun getLevelsFromRequest(webRequest: NativeWebRequest): List<QuestionLevel>? {
        val input = webRequest.getParameter("levels") ?: return null

        val levelParams = input.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        return Arrays.stream(levelParams)
            .map { value: String? -> QuestionLevel.valueOf(value!!) }
            .collect(Collectors.toList())
    }

    private fun getQuestionTypeFromRequest(webRequest: NativeWebRequest): QuestionType? {
        val input = webRequest.getParameter("questionType") ?: return null

        return QuestionType.valueOf(input)
    }

    private fun getCreatorIdFromRequest(webRequest: NativeWebRequest): Long? {
        val input = webRequest.getParameter("creatorId") ?: return null

        return input.toLong()
    }

    private fun getSortFromRequest(webRequest: NativeWebRequest): QuestionSortType? {
        val input = webRequest.getParameter("sort") ?: return null

        return QuestionSortType.valueOf(input)
    }
}
