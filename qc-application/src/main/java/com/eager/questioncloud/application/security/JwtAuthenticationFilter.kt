package com.eager.questioncloud.application.security

import com.eager.questioncloud.application.business.authentication.implement.AuthenticationTokenManager
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Component
class JwtAuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val authenticationTokenManager: AuthenticationTokenManager,
    private val userRepository: UserRepository
) : BasicAuthenticationFilter(authenticationManager) {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val accessToken = parseToken(request.getHeader("Authorization"))
            authentication(accessToken)
        } catch (e: Exception) {
            if (SecurityContextHolder.getContext().authentication == null) {
                setGuestAuthentication()
            }
        } finally {
            filterChain.doFilter(request, response)
        }
    }

    private fun parseToken(authorization: String): String? {
        if (!StringUtils.hasText(authorization)) {
            return null
        }
        if (!authorization.startsWith("Bearer ")) {
            return null
        }
        return authorization.substring(7)
    }

    private fun authentication(accessToken: String?) {
        val uid = authenticationTokenManager.parseUidFromAccessToken(accessToken)
        val userWithCreator = userRepository.getUserWithCreator(uid)
        val userPrincipal: UserPrincipal = UserPrincipal.create(userWithCreator.user, userWithCreator.creator)
        val authentication = UsernamePasswordAuthenticationToken(
            userPrincipal,
            userPrincipal.user.userInformation.name,
            userPrincipal.authorities
        )
        super.getAuthenticationManager().authenticate(authentication)
    }

    private fun setGuestAuthentication() {
        val userPrincipal: UserPrincipal = UserPrincipal.guest()
        val authentication = UsernamePasswordAuthenticationToken(
            userPrincipal,
            userPrincipal.user.userInformation.name,
            userPrincipal.authorities
        )
        super.getAuthenticationManager().authenticate(authentication)
    }
}
