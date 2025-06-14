package com.eager.questioncloud.application.api.feed.library.dto

import com.eager.questioncloud.application.api.creator.dto.CreatorInformation
import com.eager.questioncloud.core.domain.userquestion.dto.UserQuestionContent

class LibraryContent(
    val content: UserQuestionContent,
    val creator: CreatorInformation
)