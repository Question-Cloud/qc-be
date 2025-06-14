package com.eager.questioncloud.authentication.dto

import com.eager.questioncloud.authentication.model.AuthenticationToken
import com.fasterxml.jackson.annotation.JsonProperty

class LoginResponse(
    @JsonProperty("authenticationToken")
    val authenticationToken: AuthenticationToken
)