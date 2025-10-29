package com.eager.questioncloud.workspace.dto

data class CreatorProfileResponse(
    val profile: CreatorProfile
)

data class CreatorProfile(
    val mainSubject: String,
    val introduction: String
)