package com.eager.questioncloud.application.security;

import com.eager.questioncloud.application.business.authentication.implement.AuthenticationTokenManager;
import com.eager.questioncloud.core.domain.user.dto.UserWithCreator;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final AuthenticationTokenManager authenticationTokenManager;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationTokenManager authenticationTokenManager,
        UserRepository userRepository) {
        super(authenticationManager);
        this.authenticationTokenManager = authenticationTokenManager;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            String accessToken = parseToken(request.getHeader("Authorization"));
            authentication(accessToken);
        } catch (Exception e) {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                setGuestAuthentication();
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

    private void authentication(String accessToken) {
        Long uid = authenticationTokenManager.parseUidFromAccessToken(accessToken);
        UserWithCreator userWithCreator = userRepository.getUserWithCreator(uid);
        UserPrincipal userPrincipal = UserPrincipal.create(userWithCreator.getUser(), userWithCreator.getCreator());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userPrincipal,
            userPrincipal.getUser().getUserInformation().getName(),
            userPrincipal.getAuthorities());
        super.getAuthenticationManager().authenticate(authentication);
    }

    private void setGuestAuthentication() {
        UserPrincipal userPrincipal = UserPrincipal.guest();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userPrincipal,
            userPrincipal.getUser().getUserInformation().getName(),
            userPrincipal.getAuthorities());
        super.getAuthenticationManager().authenticate(authentication);
    }
}
