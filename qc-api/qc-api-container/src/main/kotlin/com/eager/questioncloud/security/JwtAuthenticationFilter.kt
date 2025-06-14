package com.eager.questioncloud.application.security

import com.eager.questioncloud.authentication.implement.AuthenticationTokenParser
import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.user.enums.UserType
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val authenticationTokenParser: AuthenticationTokenParser,
    private val userRepository: UserRepository
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

    private fun parseToken(authorization: String): String {
        if (!StringUtils.hasText(authorization)) {
            throw CoreException(Error.UNAUTHORIZED_TOKEN)
        }
        if (!authorization.startsWith("Bearer ")) {
            throw CoreException(Error.UNAUTHORIZED_TOKEN)
        }
        return authorization.substring(7)
    }

    private fun authentication(accessToken: String) {
        val claims = authenticationTokenParser.parseAccessToken(accessToken)
        val user = userRepository.getUser(claims["uid"].toString().toLong())

        val userPrincipal = UserPrincipal(user.uid)
        val authentication = UsernamePasswordAuthenticationToken(
            userPrincipal,
            user.userInformation.email,
            createAuthorities(user.userType)
        )
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun setGuestAuthentication() {
        val userPrincipal = UserPrincipal(-1L)
        val authentication = UsernamePasswordAuthenticationToken(
            userPrincipal,
            "GUEST",
            createAuthorities(UserType.Guest)
        )
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun createAuthorities(userType: UserType): Collection<GrantedAuthority> {
        when (userType) {
            UserType.NormalUser -> return@createAuthorities listOf(SimpleGrantedAuthority("ROLE_NormalUser"))
            UserType.CreatorUser -> return@createAuthorities listOf(SimpleGrantedAuthority("ROLE_CreatorUser"))
            UserType.Guest -> return@createAuthorities listOf(SimpleGrantedAuthority("ROLE_Guest"))
        }
    }
}
