package com.eager.questioncloud.creator.api.internal

interface CreatorCommandAPI {
    fun updateCreatorProfile(creatorId: Long, mainSubject: String, introduction: String)
}