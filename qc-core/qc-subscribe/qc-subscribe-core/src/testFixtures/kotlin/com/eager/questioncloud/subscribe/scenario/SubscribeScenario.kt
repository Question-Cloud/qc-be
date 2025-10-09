package com.eager.questioncloud.subscribe.scenario

import com.eager.questioncloud.creator.api.internal.CreatorQueryData
import com.eager.questioncloud.subscribe.domain.Subscribe
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

class SubscribeScenario(
    val subscribeds: MutableList<Subscribe>,
    val creatorQueryDatas: MutableList<CreatorQueryData>,
    val userQueryDatas: MutableList<UserQueryData>
) {
    companion object {
        fun create(userId: Long, count: Int = 5): SubscribeScenario {
            val subscribeds = mutableListOf<Subscribe>()
            val creatorQueryDatas = mutableListOf<CreatorQueryData>()
            val userQueryDatas = mutableListOf<UserQueryData>()
            for (i in 1L..count) {
                val creatorQueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorQueryData>()
                    .set(CreatorQueryData::userId, i)
                    .set(CreatorQueryData::creatorId, i)
                    .sample()
                
                val userQueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>()
                    .set(UserQueryData::userId, i)
                    .sample()
                
                val subscribe = Subscribe.create(userId, creatorQueryData.creatorId)
                
                subscribeds.add(subscribe)
                creatorQueryDatas.add(creatorQueryData)
                userQueryDatas.add(userQueryData)
            }
            
            return SubscribeScenario(subscribeds, creatorQueryDatas, userQueryDatas)
        }
    }
}