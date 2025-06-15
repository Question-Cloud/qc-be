package com.eager.questioncloud.creator.dto

import com.fasterxml.jackson.annotation.JsonProperty

class CreatorInformationResponse(
    @JsonProperty("creatorInformation")
    val creatorInformation: CreatorInformation
)
