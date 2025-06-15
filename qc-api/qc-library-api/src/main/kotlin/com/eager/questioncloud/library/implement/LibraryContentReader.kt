package com.eager.questioncloud.library.implement

import com.eager.questioncloud.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.library.dto.ContentCreator
import com.eager.questioncloud.library.dto.LibraryContent
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.userquestion.infrastructure.repository.UserQuestionRepository
import org.springframework.stereotype.Component

@Component
class LibraryContentReader(
    private val userQuestionRepository: UserQuestionRepository,
    private val creatorRepository: CreatorRepository,
    private val userRepository: UserRepository
) {
    fun getUserQuestions(questionFilter: QuestionFilter): List<LibraryContent> {
        val userQuestions = userQuestionRepository.getUserQuestions(questionFilter)
        val creators = creatorRepository.findByIdIn(userQuestions.map { it.creatorId })
        val creatorMap = creators.associateBy { it.id }
        val creatorUserMap = userRepository.findByUidIn(creators.map { it.userId }).associateBy { it.uid }

        return userQuestions.map {
            val creator = creatorMap.getValue(it.creatorId)
            val creatorUser = creatorUserMap.getValue(creator.userId)

            LibraryContent(
                it, ContentCreator(
                    creatorUser.userInformation.name,
                    creatorUser.userInformation.profileImage,
                    creator.mainSubject
                )
            )
        }
    }

    fun countUserQuestions(questionFilter: QuestionFilter): Int {
        return userQuestionRepository.countUserQuestions(questionFilter)
    }
}