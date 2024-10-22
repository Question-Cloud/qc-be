package com.eager.questioncloud.security;

import com.eager.questioncloud.authentication.implement.AuthenticationProcessor;
import com.eager.questioncloud.authentication.implement.AuthenticationTokenProcessor;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationTokenProcessor authenticationTokenProcessor;
    private final AuthenticationProcessor authenticationProcessor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            String accessToken = parseToken(request.getHeader("Authorization"));
            Claims claims = authenticationTokenProcessor.getAccessTokenClaimsWithValidate(accessToken);
            Long uid = claims.get("uid", Long.class);
            authenticationProcessor.authentication(uid);
        } catch (Exception e) {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticationProcessor.setGuest();
            }
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private String parseToken(String authorization) {
        if (!StringUtils.hasText(authorization)) {
            return null;
        }
        if (!authorization.startsWith("Bearer ")) {
            return null;
        }
        return authorization.substring(7);
    }
}
