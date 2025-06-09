package com.eager.questioncloud.application.api.authentication.dto

import com.fasterxml.jackson.annotation.JsonProperty

class RefreshResponse(
    @JsonProperty("authenticationToken")
    val authenticationToken: AuthenticationToken
)