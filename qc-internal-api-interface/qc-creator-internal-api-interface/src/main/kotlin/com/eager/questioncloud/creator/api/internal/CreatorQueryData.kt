package com.eager.questioncloud.creator.api.internal

class CreatorQueryData(
    val userId: Long,
    val creatorId: Long,
    val mainSubject: String,
    val rate: Double,
    val sales: Int,
    val subscriberCount: Int,
)