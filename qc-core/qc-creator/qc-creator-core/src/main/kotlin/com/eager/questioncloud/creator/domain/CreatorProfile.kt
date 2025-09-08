package com.eager.questioncloud.creator.domain

class CreatorProfile(
    val creatorId: Long,
    val name: String,
    val profileImage: String,
    val mainSubject: String,
    val email: String,
    val introduction: String,
)