package com.eager.questioncloud.post.service

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.creator.api.internal.CreatorQueryData
import com.eager.questioncloud.post.domain.Post
import com.eager.questioncloud.post.domain.PostContent
import com.eager.questioncloud.post.domain.PostFile
import com.eager.questioncloud.post.repository.PostRepository
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class PostServiceTest(
    @Autowired val postService: PostService,
    @Autowired val postRepository: PostRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var questionQueryAPI: QuestionQueryAPI
    
    @MockBean
    lateinit var creatorQueryAPI: CreatorQueryAPI
    
    @MockBean
    lateinit var userQueryAPI: UserQueryAPI
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `게시글을 등록할 수 있다`() {
        //given
        val userId = 100L
        val questionId = 200L
        val creatorId = 300L
        val creatorUserId = 400L
        
        val postContent = PostContent.create("테스트 게시글", "게시글 내용입니다", emptyList())
        val post = Post.create(questionId, userId, postContent)
        
        given(questionQueryAPI.getQuestionInformation(questionId))
            .willReturn(createQuestionInformation(questionId, creatorId))
        
        given(creatorQueryAPI.getCreator(creatorId))
            .willReturn(createCreatorQueryData(creatorUserId, creatorId))
        
        given(questionQueryAPI.isOwned(userId, questionId))
            .willReturn(true)
        
        //when
        val result = postService.register(post)
        
        //then
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.questionId).isEqualTo(questionId)
        Assertions.assertThat(result.writerId).isEqualTo(userId)
        Assertions.assertThat(result.postContent.title).isEqualTo("테스트 게시글")
        Assertions.assertThat(result.postContent.content).isEqualTo("게시글 내용입니다")
    }
    
    @Test
    fun `권한이 없으면 게시글 등록이 실패한다`() {
        //given
        val userId = 101L
        val questionId = 201L
        val creatorId = 301L
        val creatorUserId = 401L
        
        val postContent = PostContent.create("권한없는 게시글", "등록할 수 없는 내용", emptyList())
        val post = Post.create(questionId, userId, postContent)
        
        given(questionQueryAPI.getQuestionInformation(questionId))
            .willReturn(createQuestionInformation(questionId, creatorId))
        
        given(creatorQueryAPI.getCreator(creatorId))
            .willReturn(createCreatorQueryData(creatorUserId, creatorId))
        
        given(questionQueryAPI.isOwned(userId, questionId))
            .willReturn(false)
        
        //when & then
        assertThrows<CoreException> {
            postService.register(post)
        }
    }
    
    @Test
    fun `게시글 목록을 조회할 수 있다`() {
        //given
        val userId = 102L
        val questionId = 202L
        val writerId1 = 501L
        val writerId2 = 502L
        val creatorId = 302L
        val creatorUserId = 402L
        
        val post1 = createPost(questionId, writerId1, "첫 번째 게시글", "첫 번째 내용")
        val post2 = createPost(questionId, writerId2, "두 번째 게시글", "두 번째 내용")
        postRepository.save(post1)
        postRepository.save(post2)
        
        given(questionQueryAPI.getQuestionInformation(questionId))
            .willReturn(createQuestionInformation(questionId, creatorId))
        
        given(creatorQueryAPI.getCreator(creatorId))
            .willReturn(createCreatorQueryData(creatorUserId, creatorId))
        
        given(questionQueryAPI.isOwned(userId, questionId))
            .willReturn(true)
        
        val userData1 = UserQueryData(writerId1, "작성자1", "profile1.jpg", "user1@test.com")
        val userData2 = UserQueryData(writerId2, "작성자2", "profile2.jpg", "user2@test.com")
        given(userQueryAPI.getUsers(listOf(writerId1, writerId2)))
            .willReturn(listOf(userData1, userData2))
        
        val pagingInformation = PagingInformation(0, 10)
        
        //when
        val result = postService.getPostPreviews(userId, questionId, pagingInformation)
        
        //then
        Assertions.assertThat(result).hasSize(2)
        
        val preview1 = result.find { it.title == "첫 번째 게시글" }
        Assertions.assertThat(preview1).isNotNull
        Assertions.assertThat(preview1!!.writer).isEqualTo("작성자1")
        
        val preview2 = result.find { it.title == "두 번째 게시글" }
        Assertions.assertThat(preview2).isNotNull
        Assertions.assertThat(preview2!!.writer).isEqualTo("작성자2")
    }
    
    @Test
    fun `게시글 개수를 조회할 수 있다`() {
        //given
        val questionId = 203L
        val writerId = 503L
        
        val post1 = createPost(questionId, writerId, "게시글1", "내용1")
        val post2 = createPost(questionId, writerId, "게시글2", "내용2")
        val post3 = createPost(questionId, writerId, "게시글3", "내용3")
        postRepository.save(post1)
        postRepository.save(post2)
        postRepository.save(post3)
        
        //when
        val result = postService.countPost(questionId)
        
        //then
        Assertions.assertThat(result).isEqualTo(3)
    }
    
    @Test
    fun `게시글 상세 정보를 조회할 수 있다`() {
        //given
        val userId = 103L
        val questionId = 204L
        val writerId = 504L
        val creatorId = 303L
        val creatorUserId = 403L
        
        val files = listOf(
            PostFile("file1.txt", "https://example.com/file1.txt"),
            PostFile("file2.jpg", "https://example.com/file2.jpg")
        )
        val post = createPostWithFiles(questionId, writerId, "상세 게시글", "상세 내용입니다", files)
        val savedPost = postRepository.save(post)
        
        given(questionQueryAPI.getQuestionInformation(questionId))
            .willReturn(createQuestionInformation(questionId, creatorId))
        
        given(creatorQueryAPI.getCreator(creatorId))
            .willReturn(createCreatorQueryData(creatorUserId, creatorId))
        
        given(questionQueryAPI.isOwned(userId, questionId))
            .willReturn(true)
        
        val userData = UserQueryData(writerId, "게시글작성자", "profile.jpg", "writer@test.com")
        given(userQueryAPI.getUser(writerId))
            .willReturn(userData)
        
        //when
        val result = postService.getPostDetail(userId, savedPost.id)
        
        //then
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.title).isEqualTo("상세 게시글")
        Assertions.assertThat(result.content).isEqualTo("상세 내용입니다")
        Assertions.assertThat(result.files).hasSize(2)
        Assertions.assertThat(result.writer).isEqualTo("게시글작성자")
    }
    
    @Test
    fun `게시글을 수정할 수 있다`() {
        //given
        val userId = 104L
        val questionId = 205L
        
        val originalPost = createPost(questionId, userId, "원본 제목", "원본 내용")
        val savedPost = postRepository.save(originalPost)
        
        val updatedContent = PostContent.create("수정된 제목", "수정된 내용", emptyList())
        
        //when
        postService.modify(savedPost.id, userId, updatedContent)
        
        //then
        val modifiedPost = postRepository.findByIdAndWriterId(savedPost.id, userId)
        Assertions.assertThat(modifiedPost.postContent.title).isEqualTo("수정된 제목")
        Assertions.assertThat(modifiedPost.postContent.content).isEqualTo("수정된 내용")
    }
    
    @Test
    fun `게시글을 삭제할 수 있다`() {
        //given
        val userId = 105L
        val questionId = 206L
        
        val post = createPost(questionId, userId, "삭제될 게시글", "삭제될 내용")
        val savedPost = postRepository.save(post)
        
        //when
        postService.delete(savedPost.id, userId)
        
        //then
        val postCount = postRepository.countByQuestionId(questionId)
        Assertions.assertThat(postCount).isEqualTo(0)
    }
    
    @Test
    fun `크리에이터는 게시글을 등록할 수 있다`() {
        //given
        val creatorUserId = 106L
        val questionId = 207L
        val creatorId = 304L
        
        val postContent = PostContent.create("크리에이터 게시글", "크리에이터가 작성한 내용", emptyList())
        val post = Post.create(questionId, creatorUserId, postContent)
        
        given(questionQueryAPI.getQuestionInformation(questionId))
            .willReturn(createQuestionInformation(questionId, creatorId))
        
        given(creatorQueryAPI.getCreator(creatorId))
            .willReturn(createCreatorQueryData(creatorUserId, creatorId))
        
        //when
        val result = postService.register(post)
        
        //then
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.questionId).isEqualTo(questionId)
        Assertions.assertThat(result.writerId).isEqualTo(creatorUserId)
        Assertions.assertThat(result.postContent.title).isEqualTo("크리에이터 게시글")
    }
    
    private fun createPost(questionId: Long, writerId: Long, title: String, content: String): Post {
        val postContent = PostContent.create(title, content, emptyList())
        return Post.create(questionId, writerId, postContent)
    }
    
    private fun createPostWithFiles(questionId: Long, writerId: Long, title: String, content: String, files: List<PostFile>): Post {
        val postContent = PostContent.create(title, content, files)
        return Post.create(questionId, writerId, postContent)
    }
    
    private fun createQuestionInformation(questionId: Long, creatorId: Long): QuestionInformationQueryResult {
        return QuestionInformationQueryResult(
            id = questionId,
            creatorId = creatorId,
            title = "테스트 문제",
            subject = "수학",
            parentCategory = "수학",
            childCategory = "대수",
            thumbnail = "thumbnail.jpg",
            questionLevel = "중급",
            price = 1000,
            rate = 4.5
        )
    }
    
    private fun createCreatorQueryData(userId: Long, creatorId: Long): CreatorQueryData {
        return CreatorQueryData(
            userId = userId,
            creatorId = creatorId,
            mainSubject = "수학",
            rate = 4.5,
            sales = 100,
            subscriberCount = 500
        )
    }
}
