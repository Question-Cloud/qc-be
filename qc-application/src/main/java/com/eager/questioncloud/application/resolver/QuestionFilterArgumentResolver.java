package com.eager.questioncloud.application.resolver;

import com.eager.questioncloud.application.security.UserPrincipal;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.question.QuestionFilter;
import com.eager.questioncloud.core.domain.question.QuestionLevel;
import com.eager.questioncloud.core.domain.question.QuestionSortType;
import com.eager.questioncloud.core.domain.question.QuestionType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class QuestionFilterArgumentResolver implements HandlerMethodArgumentResolver {
    private final PagingInformationArgumentResolver pagingInformationArgumentResolver;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(QuestionFilter.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) throws Exception {
        Long userId = getUserIdFromRequest();
        List<Long> categories = getCategoriesFromRequest(webRequest);
        List<QuestionLevel> levels = getLevelsFromRequest(webRequest);
        QuestionType questionType = getQuestionTypeFromRequest(webRequest);
        Long creatorId = getCreatorIdFromRequest(webRequest);
        QuestionSortType sort = getSortFromRequest(webRequest);
        PagingInformation pagingInformation = pagingInformationArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        return new QuestionFilter(userId, categories, levels, questionType, creatorId, sort, pagingInformation);
    }

    private Long getUserIdFromRequest() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userPrincipal.getUser().getUid());
        return userPrincipal.getUser().getUid();
    }

    private List<Long> getCategoriesFromRequest(NativeWebRequest webRequest) {
        String input = webRequest.getParameter("categories");

        if (input == null) {
            return null;
        }

        String[] categoriesParams = input.split(",");

        return Arrays.stream(categoriesParams)
            .map(Long::parseLong)
            .collect(Collectors.toList());
    }

    private List<QuestionLevel> getLevelsFromRequest(NativeWebRequest webRequest) {
        String input = webRequest.getParameter("levels");

        if (input == null) {
            return null;
        }

        String[] levelParams = input.split(",");

        return Arrays.stream(levelParams)
            .map(QuestionLevel::valueOf)
            .collect(Collectors.toList());
    }

    private QuestionType getQuestionTypeFromRequest(NativeWebRequest webRequest) {
        String input = webRequest.getParameter("questionType");

        if (input == null) {
            return null;
        }

        return QuestionType.valueOf(input);
    }

    private Long getCreatorIdFromRequest(NativeWebRequest webRequest) {
        String input = webRequest.getParameter("creatorId");

        if (input == null) {
            return null;
        }

        return Long.parseLong(input);
    }

    private QuestionSortType getSortFromRequest(NativeWebRequest webRequest) {
        String input = webRequest.getParameter("sort");

        if (input == null) {
            return null;
        }

        return QuestionSortType.valueOf(input);
    }

    private PagingInformation getPagingInformation(NativeWebRequest webRequest) {
        int page = Integer.parseInt(webRequest.getParameter("page"));
        int size = Integer.parseInt(webRequest.getParameter("size"));

        return new PagingInformation(page, size);
    }
}
