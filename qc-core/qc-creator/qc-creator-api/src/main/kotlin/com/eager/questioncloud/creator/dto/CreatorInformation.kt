package com.eager.questioncloud.creator.dto

import com.eager.questioncloud.creator.domain.CreatorProfile

class CreatorInformation(
    val creatorProfile: CreatorProfile,
    val salesCount: Int,
    val averageRateOfReview: Double,
    val subscriberCount: Int,
)
