package com.eager.questioncloud.question.resolver

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionSortType
import com.eager.questioncloud.question.enums.QuestionType
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.*
import java.util.stream.Collectors

@Component
class QuestionFilterArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == QuestionFilter::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): QuestionFilter {
        val categories = getCategoriesFromRequest(webRequest)
        val levels = getLevelsFromRequest(webRequest)
        val questionType = getQuestionTypeFromRequest(webRequest)
        val creatorId = getCreatorIdFromRequest(webRequest)
        val sort = getSortFromRequest(webRequest)
        return QuestionFilter(categories, levels, questionType, creatorId, sort)
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

    private fun getSortFromRequest(webRequest: NativeWebRequest): QuestionSortType {
        val input = webRequest.getParameter("sort")

        if (input.isNullOrBlank()) {
            throw CoreException(Error.BAD_REQUEST)
        }

        return QuestionSortType.valueOf(input)
    }
}
