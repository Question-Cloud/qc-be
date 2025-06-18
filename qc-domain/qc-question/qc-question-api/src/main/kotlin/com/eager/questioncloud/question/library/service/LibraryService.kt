package com.eager.questioncloud.question.library.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.library.dto.LibraryContent
import com.eager.questioncloud.question.library.implement.LibraryContentReader
import org.springframework.stereotype.Service

@Service
class LibraryService(
    private val libraryContentReader: LibraryContentReader
) {
    fun getUserQuestions(
        userId: Long,
        questionFilter: QuestionFilter,
        pagingInformation: PagingInformation
    ): List<LibraryContent> {
        return libraryContentReader.getUserQuestions(userId, questionFilter, pagingInformation)
    }

    fun countUserQuestions(userId: Long, questionFilter: QuestionFilter): Int {
        return libraryContentReader.countUserQuestions(userId, questionFilter)
    }
}
