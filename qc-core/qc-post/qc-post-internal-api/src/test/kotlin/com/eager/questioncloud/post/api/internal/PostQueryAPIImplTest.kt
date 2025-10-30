package com.eager.questioncloud.post.api.internal

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.domain.Post
import com.eager.questioncloud.post.domain.PostContent
import com.eager.questioncloud.post.repository.PostRepository
import com.eager.questioncloud.test.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class PostQueryAPIImplTest(
    private val postQueryAPI: PostQueryAPIImpl,
    private val postRepository: PostRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {

    init {
        afterTest {
            dbCleaner.cleanUp()
        }

        Given("여러 문제에 대한 게시글이 존재할 때") {
            val questionId1 = 101L
            val questionId2 = 201L
            val questionId3 = 301L
            val writerId = 501L

            val post1 = createPost(questionId1, writerId, "문제1 게시글", "문제1 내용")
            val post2 = createPost(questionId2, writerId, "문제2 게시글", "문제2 내용")
            val post3 = createPost(questionId3, writerId, "문제3 게시글", "문제3 내용")
            postRepository.save(post1)
            postRepository.save(post2)
            postRepository.save(post3)

            val targetQuestionIds = listOf(questionId1, questionId2)
            val pagingInformation = PagingInformation(0, 10)

            When("특정 문제 ID 목록으로 게시글을 조회하면") {
                val result = postQueryAPI.getCreatorPosts(targetQuestionIds, pagingInformation)

                Then("해당 문제들의 게시글만 반환된다") {
                    result shouldHaveSize 2

                    val titles = result.map { it.title }
                    titles shouldContainExactlyInAnyOrder listOf("문제1 게시글", "문제2 게시글")
                    titles shouldNotContain "문제3 게시글"
                }
            }
        }

        Given("여러 문제에 대한 게시글들이 존재할 때") {
            val questionId1 = 105L
            val questionId2 = 205L
            val questionId3 = 305L
            val writerId = 505L

            val post1 = createPost(questionId1, writerId, "첫 번째", "내용1")
            val post2 = createPost(questionId1, writerId, "두 번째", "내용2")
            val post3 = createPost(questionId2, writerId, "세 번째", "내용3")
            val post4 = createPost(questionId3, writerId, "네 번째", "내용4")
            postRepository.save(post1)
            postRepository.save(post2)
            postRepository.save(post3)
            postRepository.save(post4)

            val questionIds = listOf(questionId1, questionId2, questionId3)

            When("문제 ID 목록으로 게시글 개수를 조회하면") {
                val result = postQueryAPI.countByQuestionIdIn(questionIds)

                Then("해당 문제들의 전체 게시글 개수가 반환된다") {
                    result shouldBe 4
                }
            }
        }
    }

    private fun createPost(questionId: Long, writerId: Long, title: String, content: String): Post {
        val postContent = PostContent.create(title, content, emptyList())
        return Post.create(questionId, writerId, postContent)
    }
}
