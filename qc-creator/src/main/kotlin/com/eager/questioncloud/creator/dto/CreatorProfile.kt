package com.eager.questioncloud.creator.dto

class CreatorProfile(
    val creatorId: Long,
    val name: String,
    val profileImage: String?,
    val mainSubject: String,
    val email: String,
    val introduction: String,
) {
}