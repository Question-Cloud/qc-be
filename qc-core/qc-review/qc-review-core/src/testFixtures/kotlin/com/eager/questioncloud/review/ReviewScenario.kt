package com.eager.questioncloud.review

import com.eager.questioncloud.review.domain.QuestionReview
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

class ReviewScenario(
    val reviews: List<QuestionReview>,
    val userQueryDatas: List<UserQueryData>
) {
    companion object {
        fun create(questionId: Long, count: Int = 5): ReviewScenario {
            val reviews = mutableListOf<QuestionReview>()
            val userQueryDatas = mutableListOf<UserQueryData>()
            
            for (i in 1L..count) {
                reviews.add(QuestionReview.create(questionId, i, "review $i", IntRange(1, 5).random()))
                userQueryDatas.add(
                    Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>()
                        .set(UserQueryData::userId, i)
                        .sample()
                )
            }
            
            return ReviewScenario(reviews, userQueryDatas)
        }
        
        fun createMyReveiw(questionId: Long, userId: Long): ReviewScenario {
            val reviews = mutableListOf<QuestionReview>()
            val userQueryDatas = mutableListOf<UserQueryData>()
            
            reviews.add(QuestionReview.create(questionId, 1L, "review", IntRange(1, 5).random()))
            userQueryDatas.add(
                Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>()
                    .set(UserQueryData::userId, userId)
                    .sample()
            )
            return ReviewScenario(reviews, userQueryDatas)
        }
    }
}