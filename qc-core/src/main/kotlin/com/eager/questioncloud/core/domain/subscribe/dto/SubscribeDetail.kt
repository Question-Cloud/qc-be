package com.eager.questioncloud.core.domain.subscribe.dto

import com.eager.questioncloud.core.domain.question.enums.Subject

class SubscribeDetail(
    val creatorId: Long,
    val creatorName: String,
    val creatorProfileImage: String?,
    val mainSubject: Subject,
    val introduction: String,
    val subscribeCount: Int
)
