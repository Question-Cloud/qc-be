package com.eager.questioncloud.authentication.dto

import com.eager.questioncloud.authentication.model.AuthenticationToken
import com.fasterxml.jackson.annotation.JsonProperty

class RefreshResponse(
    @JsonProperty("authenticationToken")
    val authenticationToken: AuthenticationToken
)