package com.eager.questioncloud.scenario

import com.eager.questioncloud.post.api.CreatorPostQueryAPIResult
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

class WorkspacePostScenario(
    val questionInformationQueryResults: List<QuestionInformationQueryResult>,
    val creatorPostQueryAPIResults: List<CreatorPostQueryAPIResult>,
    val userQueryDatas: List<UserQueryData>,
) {
    companion object {
        fun create(count: Int): WorkspacePostScenario {
            val creatorId = 1L
            var postId = 1L
            
            val userQueryDatas = (1..count).map {
                Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>()
                    .set(UserQueryData::userId, it)
                    .sample()
            }
            
            val questionInformationQueryResults = (1..count).map {
                Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                    .set(QuestionInformationQueryResult::id, it)
                    .set(QuestionInformationQueryResult::creatorId, creatorId)
                    .sample()
            }
            
            val creatorPostQueryAPIResults = (1..count).map {
                Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorPostQueryAPIResult>()
                    .set(CreatorPostQueryAPIResult::id, postId++)
                    .set(CreatorPostQueryAPIResult::writerId, (1..count).random())
                    .sample()
            }
            
            return WorkspacePostScenario(questionInformationQueryResults, creatorPostQueryAPIResults, userQueryDatas)
        }
    }
}