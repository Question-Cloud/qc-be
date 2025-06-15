package com.eager.questioncloud.library.service

import com.eager.questioncloud.library.dto.LibraryContent
import com.eager.questioncloud.library.implement.LibraryContentReader
import com.eager.questioncloud.question.common.QuestionFilter
import org.springframework.stereotype.Service

@Service
class LibraryService(
    private val libraryContentReader: LibraryContentReader
) {
    fun getUserQuestions(questionFilter: QuestionFilter): List<LibraryContent> {
        return libraryContentReader.getUserQuestions(questionFilter)
    }

    fun countUserQuestions(questionFilter: QuestionFilter): Int {
        return libraryContentReader.countUserQuestions(questionFilter)
    }
}
