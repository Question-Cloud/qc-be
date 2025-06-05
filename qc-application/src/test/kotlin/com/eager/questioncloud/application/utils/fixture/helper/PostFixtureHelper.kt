package com.eager.questioncloud.application.utils.fixture.helper

import com.eager.questioncloud.application.utils.fixture.Fixture
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostRepository
import com.eager.questioncloud.core.domain.post.model.Post
import com.eager.questioncloud.core.domain.post.model.PostContent

class PostFixtureHelper {
    companion object {
        fun createPost(
            writerId: Long,
            questionId: Long,
            postRepository: PostRepository
        ): Post {
            return postRepository.save(
                Post.create(
                    questionId = questionId,
                    writerId = writerId,
                    postContent = PostContent.create("테스트 포스트 제목", "테스트 포스트 내용입니다.", emptyList())
                )
            )
        }

        fun createPost(
            writerId: Long,
            questionId: Long,
            title: String,
            content: String,
            postRepository: PostRepository
        ): Post {
            return postRepository.save(
                Post.create(
                    questionId = questionId,
                    writerId = writerId,
                    postContent = PostContent.create(title, content, emptyList())
                )
            )
        }
    }
}
