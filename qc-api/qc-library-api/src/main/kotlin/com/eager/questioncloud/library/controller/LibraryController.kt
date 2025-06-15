package com.eager.questioncloud.library.controller

import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.library.dto.LibraryContent
import com.eager.questioncloud.library.service.LibraryService
import com.eager.questioncloud.question.common.QuestionFilter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/library")
class LibraryController(
    private val libraryService: LibraryService
) {
    @GetMapping
    fun getUserQuestionLibraryList(questionFilter: QuestionFilter): PagingResponse<LibraryContent> {
        val total = libraryService.countUserQuestions(questionFilter)
        val userQuestions = libraryService.getUserQuestions(questionFilter)
        return PagingResponse(total, userQuestions)
    }
}
