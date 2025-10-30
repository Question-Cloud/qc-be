package com.eager.questioncloud.post.api.internal

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.domain.Post
import com.eager.questioncloud.post.domain.PostContent
import com.eager.questioncloud.post.repository.PostRepository
import com.eager.questioncloud.test.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class PostQueryAPIImplTest(
    @Autowired val postQueryAPI: PostQueryAPIImpl,
    @Autowired val postRepository: PostRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `특정 크리에이터가 만든 문제에 해당하는 게시글을 조회할 수 있다`() {
        //given
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
        
        //when
        val result = postQueryAPI.getCreatorPosts(targetQuestionIds, pagingInformation)
        
        //then
        Assertions.assertThat(result).hasSize(2)
        
        val titles = result.map { it.title }
        Assertions.assertThat(titles).containsExactlyInAnyOrder("문제1 게시글", "문제2 게시글")
        Assertions.assertThat(titles).doesNotContain("문제3 게시글")
    }
    
    @Test
    fun `문제 ID 목록으로 게시글 개수를 조회할 수 있다`() {
        //given
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
        
        //when
        val result = postQueryAPI.countByQuestionIdIn(questionIds)
        
        //then
        Assertions.assertThat(result).isEqualTo(4)
    }
    
    private fun createPost(questionId: Long, writerId: Long, title: String, content: String): Post {
        val postContent = PostContent.create(title, content, emptyList())
        return Post.create(questionId, writerId, postContent)
    }
}
