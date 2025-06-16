package com.eager.questioncloud.social

import com.fasterxml.jackson.annotation.JsonProperty

class SocialAccessToken(
    @JsonProperty("access_token")
    val access_token: String
)