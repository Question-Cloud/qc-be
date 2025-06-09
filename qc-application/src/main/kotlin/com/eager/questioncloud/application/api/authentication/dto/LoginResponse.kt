package com.eager.questioncloud.application.api.authentication.dto

import com.fasterxml.jackson.annotation.JsonProperty

class LoginResponse(
    @JsonProperty("authenticationToken")
    val authenticationToken: AuthenticationToken
)