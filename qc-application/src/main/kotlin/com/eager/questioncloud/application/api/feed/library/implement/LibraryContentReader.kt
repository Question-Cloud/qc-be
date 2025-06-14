package com.eager.questioncloud.application.api.feed.library.implement

import com.eager.questioncloud.application.api.creator.implement.CreatorInformationReader
import com.eager.questioncloud.application.api.feed.library.dto.LibraryContent
import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import org.springframework.stereotype.Component

@Component
class LibraryContentReader(
    private val userQuestionRepository: UserQuestionRepository,
    private val creatorInformationReader: CreatorInformationReader,
) {
    fun getUserQuestions(questionFilter: QuestionFilter): List<LibraryContent> {
        val userQuestions = userQuestionRepository.getUserQuestions(questionFilter)
        val creatorInformationMap = creatorInformationReader.getCreatorInformation(userQuestions.map { it.creatorId })
            .associateBy { it.creatorProfile.creatorId }

        return userQuestions.map {
            LibraryContent(it, creatorInformationMap.getValue(it.creatorId))
        }
    }

    fun countUserQuestions(questionFilter: QuestionFilter): Int {
        return userQuestionRepository.countUserQuestions(questionFilter)
    }
}