package com.eager.questioncloud.core.domain.creator.model

import com.eager.questioncloud.core.domain.question.enums.Subject

class CreatorProfile(
    var mainSubject: Subject,
    var introduction: String
) {
    companion object {
        fun create(mainSubject: Subject, introduction: String): CreatorProfile {
            return CreatorProfile(mainSubject, introduction)
        }
    }
}
