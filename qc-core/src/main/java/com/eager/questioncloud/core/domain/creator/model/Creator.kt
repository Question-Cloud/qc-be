package com.eager.questioncloud.core.domain.creator.model


class Creator(
    val id: Long? = null,
    val userId: Long,
    var creatorProfile: CreatorProfile
) {
    fun updateProfile(creatorProfile: CreatorProfile) {
        this.creatorProfile = creatorProfile
    }

    companion object {
        @JvmStatic
        fun create(userId: Long, creatorProfile: CreatorProfile): Creator {
            return Creator(userId = userId, creatorProfile = creatorProfile)
        }
    }
}
