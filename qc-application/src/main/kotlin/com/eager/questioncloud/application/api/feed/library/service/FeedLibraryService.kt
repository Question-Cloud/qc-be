package com.eager.questioncloud.application.api.feed.library.service

import com.eager.questioncloud.application.api.feed.library.dto.LibraryContent
import com.eager.questioncloud.application.api.feed.library.implement.LibraryContentReader
import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import org.springframework.stereotype.Service

@Service
class FeedLibraryService(
    private val libraryContentReader: LibraryContentReader
) {
    fun getUserQuestions(questionFilter: QuestionFilter): List<LibraryContent> {
        return libraryContentReader.getUserQuestions(questionFilter)
    }

    fun countUserQuestions(questionFilter: QuestionFilter): Int {
        return libraryContentReader.countUserQuestions(questionFilter)
    }
}
