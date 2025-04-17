package com.eager.questioncloud.application.utils.fixture.helper

import com.eager.questioncloud.application.utils.fixture.Fixture
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question

class QuestionFixtureHelper {
    companion object {
        fun createQuestion(
            creatorId: Long,
            questionStatus: QuestionStatus = QuestionStatus.Available,
            questionRepository: QuestionRepository
        ): Question {
            return questionRepository.save(
                Fixture.fixtureMonkey.giveMeBuilder(Question::class.java)
                    .set("id", null)
                    .set("creatorId", creatorId)
                    .set("questionContent.questionCategoryId", 25L)
                    .set("questionContent.price", 1000)
                    .set("questionStatus", questionStatus)
                    .sample()
            )
        }
    }
}