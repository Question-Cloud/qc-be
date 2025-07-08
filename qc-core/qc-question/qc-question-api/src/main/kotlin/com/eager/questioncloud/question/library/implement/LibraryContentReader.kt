package com.eager.questioncloud.question.library.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.question.library.dto.ContentCreator
import com.eager.questioncloud.question.library.dto.LibraryContent
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import org.springframework.stereotype.Component

@Component
class LibraryContentReader(
    private val userQuestionRepository: UserQuestionRepository,
    private val creatorQueryAPI: CreatorQueryAPI,
    private val userQueryAPI: UserQueryAPI
) {
    fun getUserQuestions(
        userId: Long,
        questionFilter: QuestionFilter,
        pagingInformation: PagingInformation
    ): List<LibraryContent> {
        val userQuestions = userQuestionRepository.getUserQuestions(userId, questionFilter, pagingInformation)
        val creators = creatorQueryAPI.getCreators(userQuestions.map { it.creatorId })
        val creatorMap = creators.associateBy { it.creatorId }
        val creatorUserMap = userQueryAPI.getUsers(creators.map { it.userId }).associateBy { it.userId }

        return userQuestions.map {
            val creator = creatorMap.getValue(it.creatorId)
            val creatorUser = creatorUserMap.getValue(creator.userId)

            LibraryContent(
                it,
                ContentCreator(
                    creatorUser.name,
                    creatorUser.profileImage,
                    creator.mainSubject
                )
            )
        }
    }

    fun countUserQuestions(userId: Long, questionFilter: QuestionFilter): Int {
        return userQuestionRepository.countUserQuestions(userId, questionFilter)
    }
}