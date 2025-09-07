package com.eager.questioncloud.user.authentication.dto

import com.eager.questioncloud.user.authentication.model.AuthenticationToken

class RefreshResponse(
    val authenticationToken: AuthenticationToken
)