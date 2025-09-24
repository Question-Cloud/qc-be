package com.eager.questioncloud.cart.scenario

import com.eager.questioncloud.creator.api.internal.CreatorQueryData
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

class CartItemScenario(
    val cartItemQuestionIds: List<Long>,
    val questionInformationQueryResults: List<QuestionInformationQueryResult>,
    val creatorQueryDatas: List<CreatorQueryData>,
    val creatorUserQueryDatas: List<UserQueryData>,
) {
    companion object {
        fun create(itemCount: Int): CartItemScenario {
            val questionIds = (1L until 1L + itemCount).toList()
            val creatorIds = (1L until 1L + itemCount).toList()
            val creatorUserIds = (1L until 1L + itemCount).toList()
            
            val questionInformationQueryResults = questionIds.mapIndexed { index, questionId ->
                Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                    .set(QuestionInformationQueryResult::id, questionId)
                    .set(QuestionInformationQueryResult::creatorId, creatorIds[index])
                    .sample()
            }
            
            val creatorQueryDatas = creatorIds.mapIndexed { index, creatorId ->
                Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorQueryData>()
                    .set(CreatorQueryData::creatorId, creatorId)
                    .set(CreatorQueryData::userId, creatorUserIds[index])
                    .sample()
            }
            
            val creatorUserQueryDatas = creatorUserIds.map { userId ->
                Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>()
                    .set(UserQueryData::userId, userId)
                    .sample()
            }
            
            return CartItemScenario(questionIds, questionInformationQueryResults, creatorQueryDatas, creatorUserQueryDatas)
        }
    }
}