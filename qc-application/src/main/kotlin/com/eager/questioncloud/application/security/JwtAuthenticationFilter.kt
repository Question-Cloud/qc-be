package com.eager.questioncloud.application.security

import com.eager.questioncloud.application.api.authentication.implement.AuthenticationTokenManager
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val authenticationTokenManager: AuthenticationTokenManager,
    private val userRepository: UserRepository,
    private val creatorRepository: CreatorRepository,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        runCatching {
            val accessToken = parseToken(request.getHeader("Authorization"))
            authentication(accessToken)
        }.onFailure {
            if (SecurityContextHolder.getContext().authentication == null) {
                setGuestAuthentication()
            }
        }.also { filterChain.doFilter(request, response) }
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
        val user = userRepository.getUser(uid)
        val creator = creatorRepository.findByUserId(uid)
        val userPrincipal: UserPrincipal = UserPrincipal.create(user, creator)
        val authentication = UsernamePasswordAuthenticationToken(
            userPrincipal,
            userPrincipal.user.userInformation.name,
            userPrincipal.authorities
        )
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun setGuestAuthentication() {
        val userPrincipal: UserPrincipal = UserPrincipal.guest()
        val authentication = UsernamePasswordAuthenticationToken(
            userPrincipal,
            userPrincipal.user.userInformation.name,
            userPrincipal.authorities
        )
        SecurityContextHolder.getContext().authentication = authentication
    }
}
