package com.eager.questioncloud.core.domain.creator.dto

import com.eager.questioncloud.core.domain.question.enums.Subject

class CreatorInformation(
    val creatorId: Long,
    val name: String,
    val profileImage: String,
    val mainSubject: Subject,
    val email: String,
    val salesCount: Int,
    val rate: Double,
    val introduction: String,
) {
}
