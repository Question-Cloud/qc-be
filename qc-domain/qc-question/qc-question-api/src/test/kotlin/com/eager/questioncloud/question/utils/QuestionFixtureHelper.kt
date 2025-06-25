package com.eager.questioncloud.question.utils

import com.eager.questioncloud.question.domain.Question
import com.eager.questioncloud.question.domain.QuestionMetadata
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionStatus
import com.eager.questioncloud.question.enums.QuestionType
import com.eager.questioncloud.question.infrastructure.repository.QuestionMetadataRepository
import com.eager.questioncloud.question.infrastructure.repository.QuestionRepository

class QuestionFixtureHelper {
    companion object {
        private val TARGET_NUMBERS = listOf(
            2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20, 21, 22,
            24, 25, 26, 27, 28, 29, 30, 32, 33, 34, 35, 36
        )

        fun createQuestion(
            creatorId: Long,
            questionStatus: QuestionStatus = QuestionStatus.Available,
            questionType: QuestionType = QuestionType.SelfMade,
            questionLevel: QuestionLevel = QuestionLevel.LEVEL1,
            questionRepository: QuestionRepository,
            questionMetadataRepository: QuestionMetadataRepository? = null
        ): Question {
            val question = questionRepository.save(
                Fixture.fixtureMonkey.giveMeBuilder(Question::class.java)
                    .set("id", null)
                    .set("creatorId", creatorId)
                    .set("questionContent.questionCategoryId", TARGET_NUMBERS.random().toLong())
                    .set("questionContent.price", 1000)
                    .set("questionContent.questionType", questionType)
                    .set("questionContent.questionLevel", questionLevel)
                    .set("questionStatus", questionStatus)
                    .sample()
            )
            
            // QuestionMetadata 생성
            questionMetadataRepository?.let {
                val questionMetadata = QuestionMetadata(
                    questionId = question.id,
                    sales = 0,
                    reviewCount = 0,
                    totalRate = 0,
                    reviewAverageRate = 0.0
                )
                it.save(questionMetadata)
            }
            
            return question
        }

        fun createQuestion(
            creatorId: Long,
            category: Long,
            questionStatus: QuestionStatus = QuestionStatus.Available,
            questionType: QuestionType = QuestionType.SelfMade,
            questionLevel: QuestionLevel = QuestionLevel.LEVEL1,
            questionRepository: QuestionRepository,
            questionMetadataRepository: QuestionMetadataRepository? = null
        ): Question {
            val question = questionRepository.save(
                Fixture.fixtureMonkey.giveMeBuilder(Question::class.java)
                    .set("id", null)
                    .set("creatorId", creatorId)
                    .set("questionContent.questionCategoryId", category)
                    .set("questionContent.price", 1000)
                    .set("questionContent.questionType", questionType)
                    .set("questionContent.questionLevel", questionLevel)
                    .set("questionStatus", questionStatus)
                    .sample()
            )
            
            // QuestionMetadata 생성
            questionMetadataRepository?.let {
                val questionMetadata = QuestionMetadata(
                    questionId = question.id,
                    sales = 0,
                    reviewCount = 0,
                    totalRate = 0,
                    reviewAverageRate = 0.0
                )
                it.save(questionMetadata)
            }
            
            return question
        }
    }
}