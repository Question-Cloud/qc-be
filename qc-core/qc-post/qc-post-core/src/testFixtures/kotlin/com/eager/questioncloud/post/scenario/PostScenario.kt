package com.eager.questioncloud.post.scenario

import com.eager.questioncloud.post.domain.Post
import com.eager.questioncloud.post.domain.PostContent
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

class PostScenario(
    val posts: List<Post>,
    val userQueryDatas: List<UserQueryData>
) {
    companion object {
        fun create(questionId: Long, count: Int = 5): PostScenario {
            val posts = mutableListOf<Post>()
            val userQueryDatas = mutableListOf<UserQueryData>()

            for (i in 1L..count) {
                val postContent = PostContent.create(
                    "게시글 제목 $i",
                    "게시글 내용 $i",
                    emptyList()
                )
                posts.add(Post.create(questionId, i, postContent))

                userQueryDatas.add(
                    Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>()
                        .set(UserQueryData::userId, i)
                        .sample()
                )
            }

            return PostScenario(posts, userQueryDatas)
        }

        fun createSinglePost(questionId: Long, writerId: Long): PostScenario {
            val postContent = PostContent.create(
                "단일 게시글",
                "단일 게시글 내용",
                emptyList()
            )
            val post = Post.create(questionId, writerId, postContent)

            val userQueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>()
                .set(UserQueryData::userId, writerId)
                .sample()

            return PostScenario(listOf(post), listOf(userQueryData))
        }
    }
}