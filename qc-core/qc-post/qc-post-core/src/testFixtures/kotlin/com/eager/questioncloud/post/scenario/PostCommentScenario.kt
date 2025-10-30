package com.eager.questioncloud.post.scenario

import com.eager.questioncloud.post.domain.PostComment
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.test.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.KotlinTypeDefaultArbitraryBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

class PostCommentScenario(
    val postComments: MutableList<PostComment>,
    val userQueryDatas: MutableList<UserQueryData>,
) {
    companion object {
        fun create(postId: Long, count: Int = 5): PostCommentScenario {
            val postComments = (1L..count).map {
                PostComment.create(postId, it, "comment $it", false)
            }.toMutableList()
            
            val userQueryDatas = (1L..count).map {
                Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>()
                    .set(UserQueryData::userId, it)
                    .sample()
            }.toMutableList()
            
            return PostCommentScenario(postComments, userQueryDatas)
        }
    }
}

fun PostComment.custom(block: KotlinTypeDefaultArbitraryBuilder<PostComment>.() -> KotlinTypeDefaultArbitraryBuilder<PostComment>): PostComment {
    return Fixture.fixtureMonkey.giveMeKotlinBuilder<PostComment>(this)
        .block()
        .sample()
}