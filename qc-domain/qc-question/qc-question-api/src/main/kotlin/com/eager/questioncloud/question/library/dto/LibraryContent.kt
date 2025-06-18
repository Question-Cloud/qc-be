package com.eager.questioncloud.question.library.dto

import com.eager.questioncloud.question.dto.UserQuestionContent

class LibraryContent(
    val content: UserQuestionContent,
    val creator: ContentCreator
)

class ContentCreator(
    val name: String,
    val profileImage: String,
    val mainSubject: String
)