package com.eager.questioncloud.core.domain.creator.model

import com.eager.questioncloud.core.domain.question.enums.Subject


class Creator(
    val id: Long = 0,
    val userId: Long,
    var mainSubject: Subject,
    var introduction: String,
) {
    fun updateProfile(mainSubject: Subject, introduction: String) {
        this.mainSubject = mainSubject
        this.introduction = introduction
    }

    companion object {
        fun create(userId: Long, mainSubject: Subject, introduction: String): Creator {
            return Creator(userId = userId, mainSubject = mainSubject, introduction = introduction)
        }
    }
}
