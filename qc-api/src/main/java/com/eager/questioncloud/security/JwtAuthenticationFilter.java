package com.eager.questioncloud.security;

import com.eager.questioncloud.core.domain.authentication.implement.AuthenticationTokenProcessor;
import com.eager.questioncloud.core.domain.user.dto.UserWithCreator;
import com.eager.questioncloud.core.domain.user.implement.UserReader;
import com.eager.questioncloud.core.domain.user.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationTokenProcessor authenticationTokenProcessor;
    private final UserReader userReader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            String accessToken = parseToken(request.getHeader("Authorization"));
            Long uid = authenticationTokenProcessor.parseUidFromAccessToken(accessToken);
            authentication(uid);
        } catch (Exception e) {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                setGuest();
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

    private void authentication(Long uid) {
        UserWithCreator userWithCreator = userReader.getUserWithCreator(uid);
        UserPrincipal userPrincipal = UserPrincipal.create(userWithCreator.user(), userWithCreator.creator());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userPrincipal,
            userPrincipal.getUser().getUsername(),
            userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private void setGuest() {
        User guest = User.guest();
        UserPrincipal userPrincipal = UserPrincipal.create(guest, null);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userPrincipal,
            guest.getPassword(),
            guest.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
