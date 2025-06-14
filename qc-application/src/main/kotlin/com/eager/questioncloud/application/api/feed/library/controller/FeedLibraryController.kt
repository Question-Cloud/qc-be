package com.eager.questioncloud.application.api.feed.library.controller

import com.eager.questioncloud.application.api.common.PagingResponse
import com.eager.questioncloud.application.api.feed.library.dto.LibraryContent
import com.eager.questioncloud.application.api.feed.library.service.FeedLibraryService
import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feed/library")
class FeedLibraryController(
    private val feedLibraryService: FeedLibraryService
) {
    @GetMapping
    fun getUserQuestionLibraryList(questionFilter: QuestionFilter): PagingResponse<LibraryContent> {
        val total = feedLibraryService.countUserQuestions(questionFilter)
        val userQuestions = feedLibraryService.getUserQuestions(questionFilter)
        return PagingResponse(total, userQuestions)
    }
}
