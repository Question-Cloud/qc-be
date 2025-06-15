package com.eager.questioncloud.library.dto

import com.eager.questioncloud.userquestion.dto.UserQuestionContent

class LibraryContent(
    val content: UserQuestionContent,
    val creator: ContentCreator
)

class ContentCreator(
    val name: String,
    val profileImage: String,
    val mainSubject: String
)