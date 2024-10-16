package com.eager.questioncloud.resolver;

import com.eager.questioncloud.question.dto.QuestionFilter;
import com.eager.questioncloud.question.model.QuestionLevel;
import com.eager.questioncloud.question.model.QuestionSortType;
import com.eager.questioncloud.question.model.QuestionType;
import com.eager.questioncloud.security.UserPrincipal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class QuestionFilterArgumentResolver implements HandlerMethodArgumentResolver {

    private final PageableHandlerMethodArgumentResolver pageableResolver;

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
        Pageable pageable = pageableResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        return new QuestionFilter(userId, categories, levels, questionType, creatorId, sort, pageable);
    }

    private Long getUserIdFromRequest() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
}
