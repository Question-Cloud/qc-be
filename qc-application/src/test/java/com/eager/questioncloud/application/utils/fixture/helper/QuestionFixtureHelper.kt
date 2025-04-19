package com.eager.questioncloud.application.utils.fixture.helper

import com.eager.questioncloud.application.utils.fixture.Fixture
import com.eager.questioncloud.core.domain.question.enums.QuestionLevel
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus
import com.eager.questioncloud.core.domain.question.enums.QuestionType
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question

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
            questionRepository: QuestionRepository
        ): Question {
            return questionRepository.save(
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
        }

        fun createQuestion(
            creatorId: Long,
            category: Long,
            questionStatus: QuestionStatus = QuestionStatus.Available,
            questionType: QuestionType = QuestionType.SelfMade,
            questionLevel: QuestionLevel = QuestionLevel.LEVEL1,
            questionRepository: QuestionRepository,
        ): Question {
            return questionRepository.save(
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
        }
    }
}