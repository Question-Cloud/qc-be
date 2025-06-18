package com.eager.questioncloud.user.authentication.dto

import com.eager.questioncloud.user.authentication.model.AuthenticationToken
import com.fasterxml.jackson.annotation.JsonProperty

class LoginResponse(
    @JsonProperty("authenticationToken")
    val authenticationToken: AuthenticationToken
)