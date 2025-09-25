package com.eager.questioncloud.scenario

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.domain.CreatorStatistics
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

class CreatorScenario(
    val creators: List<Creator>,
    val creatorStatisticses: List<CreatorStatistics>,
    val creatorUserQueryDatas: List<UserQueryData>,
) {
    companion object {
        fun create(count: Int): CreatorScenario {
            val creators = mutableListOf<Creator>()
            val creatorStatisticses = mutableListOf<CreatorStatistics>()
            val creatorUserQueryDatas = mutableListOf<UserQueryData>()
            
            for (i in 1L until 1L + count) {
                creators.add(
                    Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                        .set(Creator::id, i)
                        .set(Creator::userId, i)
                        .sample()
                )
                creatorStatisticses.add(CreatorStatistics.create(i))
                creatorUserQueryDatas.add(
                    Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>()
                        .set(UserQueryData::userId, i)
                        .sample()
                )
            }
            return CreatorScenario(creators, creatorStatisticses, creatorUserQueryDatas)
        }
    }
}