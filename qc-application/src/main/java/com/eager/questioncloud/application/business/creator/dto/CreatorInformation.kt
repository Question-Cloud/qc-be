package com.eager.questioncloud.application.business.creator.dto

import com.eager.questioncloud.core.domain.creator.dto.CreatorProfile

class CreatorInformation(
    val creatorProfile: CreatorProfile,
    val salesCount: Int,
    val averageRateOfReview: Double,
    val subscriberCount: Int,
)
