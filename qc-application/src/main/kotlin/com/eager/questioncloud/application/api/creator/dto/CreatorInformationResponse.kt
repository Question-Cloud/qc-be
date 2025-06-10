package com.eager.questioncloud.application.api.creator.dto

import com.fasterxml.jackson.annotation.JsonProperty

class CreatorInformationResponse(
    @JsonProperty("creatorInformation")
    val creatorInformation: CreatorInformation
)
