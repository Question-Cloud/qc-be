package com.eager.questioncloud.application.resolver;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
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
            int page = Integer.parseInt(webRequest.getParameter("page")) - 1;
            int size = Integer.parseInt(webRequest.getParameter("size"));

            if (page < 0) {
                throw new CoreException(Error.BAD_REQUEST);
            }

            return new PagingInformation(page * size, size);
        } catch (Exception e) {
            throw new CoreException(Error.BAD_REQUEST);
        }
    }
}
