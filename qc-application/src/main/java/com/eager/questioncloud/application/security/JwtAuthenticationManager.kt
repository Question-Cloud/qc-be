package com.eager.questioncloud.application.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationManager : AuthenticationManager {
    override fun authenticate(authentication: Authentication): Authentication {
        SecurityContextHolder.getContext().authentication = authentication
        return authentication
    }
}
