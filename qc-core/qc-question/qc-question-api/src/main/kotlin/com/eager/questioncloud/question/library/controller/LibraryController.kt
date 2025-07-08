package com.eager.questioncloud.question.library.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.library.dto.LibraryContent
import com.eager.questioncloud.question.library.service.LibraryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/library")
class LibraryController(
    private val libraryService: LibraryService
) {
    @GetMapping
    fun getUserQuestionLibraryList(
        userPrincipal: UserPrincipal,
        questionFilter: QuestionFilter,
        pagingInformation: PagingInformation
    ): PagingResponse<LibraryContent> {
        val total = libraryService.countUserQuestions(userPrincipal.userId, questionFilter)
        val userQuestions = libraryService.getUserQuestions(userPrincipal.userId, questionFilter, pagingInformation)
        return PagingResponse(total, userQuestions)
    }
}
