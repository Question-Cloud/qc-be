package com.eager.questioncloud.creator.domain

class Creator(
    val id: Long = 0,
    val userId: Long,
    var mainSubject: String,
    var introduction: String,
) {
    fun updateProfile(mainSubject: String, introduction: String) {
        this.mainSubject = mainSubject
        this.introduction = introduction
    }

    companion object {
        fun create(userId: Long, mainSubject: String, introduction: String): Creator {
            return Creator(userId = userId, mainSubject = mainSubject, introduction = introduction)
        }
    }
}
