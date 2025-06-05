package com.eager.questioncloud.application.utils.fixture.helper

import com.eager.questioncloud.application.utils.fixture.Fixture
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReview
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

class QuestionReviewFixtureHelper {
    companion object {
        fun createQuestinoReview(
            uid: Long,
            questionId: Long,
            questionReviewRepository: QuestionReviewRepository
        ): QuestionReview {
            return questionReviewRepository.save(
                Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionReview>()
                    .set(QuestionReview::id, null)
                    .set(QuestionReview::reviewerId, uid)
                    .set(QuestionReview::questionId, questionId)
                    .set(QuestionReview::isDeleted, false)
                    .sample()
            )
        }
    }
}