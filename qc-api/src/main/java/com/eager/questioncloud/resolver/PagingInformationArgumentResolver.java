package com.eager.questioncloud.resolver;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class PagingInformationArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(PagingInformation.class);
    }

    @Override
    public PagingInformation resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) throws Exception {
        try {
            int page = Integer.parseInt(webRequest.getParameter("page"));
            int size = Integer.parseInt(webRequest.getParameter("size"));
            return new PagingInformation(page, size);
        } catch (Exception e) {
            throw new CustomException(Error.BAD_REQUEST);
        }
    }
}
