package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.domain.Post
import com.eager.questioncloud.post.domain.PostContent
import com.eager.questioncloud.post.domain.PostFile
import com.eager.questioncloud.post.infrastructure.repository.PostRepository
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
class PostReaderTest(
    @Autowired val postReader: PostReader,
    @Autowired val postRepository: PostRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var postPermissionChecker: PostPermissionChecker

    @MockBean
    lateinit var questionQueryAPI: QuestionQueryAPI

    @MockBean
    lateinit var userQueryAPI: UserQueryAPI

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `권한이 있는 사용자는 게시글 목록을 조회할 수 있다`() {
        //given
        val userId = 1L
        val questionId = 100L
        val writerId1 = 301L
        val writerId2 = 302L

        val post1 = createPost(questionId, writerId1, "첫 번째 게시글", "첫 번째 내용")
        val post2 = createPost(questionId, writerId2, "두 번째 게시글", "두 번째 내용")
        val savedPost1 = postRepository.save(post1)
        val savedPost2 = postRepository.save(post2)

        given(postPermissionChecker.hasPermission(userId, questionId))
            .willReturn(true)

        val userQueryData1 = UserQueryData(writerId1, "작성자1", "profile1.jpg", "user1@test.com")
        val userQueryData2 = UserQueryData(writerId2, "작성자2", "profile2.jpg", "user2@test.com")
        given(userQueryAPI.getUsers(listOf(writerId1, writerId2)))
            .willReturn(listOf(userQueryData1, userQueryData2))

        val pagingInformation = PagingInformation(0, 10)

        //when
        val result = postReader.getPostPreviews(userId, questionId, pagingInformation)

        //then
        Assertions.assertThat(result).hasSize(2)

        val preview1 = result.find { it.id == savedPost1.id }
        Assertions.assertThat(preview1).isNotNull
        Assertions.assertThat(preview1!!.title).isEqualTo("첫 번째 게시글")
        Assertions.assertThat(preview1.writer).isEqualTo("작성자1")

        val preview2 = result.find { it.id == savedPost2.id }
        Assertions.assertThat(preview2).isNotNull
        Assertions.assertThat(preview2!!.title).isEqualTo("두 번째 게시글")
        Assertions.assertThat(preview2.writer).isEqualTo("작성자2")
    }

    @Test
    fun `권한이 없는 사용자가 게시글 목록을 조회하면 예외가 발생한다`() {
        //given
        val userId = 2L
        val questionId = 200L

        given(postPermissionChecker.hasPermission(userId, questionId))
            .willReturn(false)

        val pagingInformation = PagingInformation(0, 10)

        //when & then
        assertThrows<CoreException> {
            postReader.getPostPreviews(userId, questionId, pagingInformation)
        }
    }

    @Test
    fun `게시글 개수를 조회할 수 있다`() {
        //given
        val questionId = 400L
        val writerId = 501L

        val post1 = createPost(questionId, writerId, "게시글1", "내용1")
        val post2 = createPost(questionId, writerId, "게시글2", "내용2")
        val post3 = createPost(questionId, writerId, "게시글3", "내용3")
        postRepository.save(post1)
        postRepository.save(post2)
        postRepository.save(post3)

        //when
        val result = postReader.countPost(questionId)

        //then
        Assertions.assertThat(result).isEqualTo(3)
    }

    @Test
    fun `권한이 있는 사용자는 게시글 상세 정보를 조회할 수 있다`() {
        //given
        val userId = 5L
        val questionId = 500L
        val writerId = 701L
        val creatorId = 801L

        val files = listOf(
            PostFile("file1.txt", "https://example.com/file1.txt"),
            PostFile("file2.jpg", "https://example.com/file2.jpg")
        )
        val post = createPostWithFiles(questionId, writerId, "상세 게시글", "상세 내용입니다.", files)
        val savedPost = postRepository.save(post)

        given(postPermissionChecker.hasPermission(userId, savedPost.questionId))
            .willReturn(true)

        val questionInformation = QuestionInformationQueryResult(
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
        given(questionQueryAPI.getQuestionInformation(savedPost.questionId))
            .willReturn(questionInformation)

        val userQueryData = UserQueryData(writerId, "게시글작성자", "profile.jpg", "writer@test.com")
        given(userQueryAPI.getUser(writerId))
            .willReturn(userQueryData)

        //when
        val result = postReader.getPostDetail(userId, savedPost.id)

        //then
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.id).isEqualTo(savedPost.id)
        Assertions.assertThat(result.questionId).isEqualTo(savedPost.questionId)
        Assertions.assertThat(result.title).isEqualTo("상세 게시글")
        Assertions.assertThat(result.content).isEqualTo("상세 내용입니다.")
        Assertions.assertThat(result.files).hasSize(2)
        Assertions.assertThat(result.parentCategory).isEqualTo("수학")
        Assertions.assertThat(result.childCategory).isEqualTo("대수")
        Assertions.assertThat(result.questionTitle).isEqualTo("테스트 문제")
        Assertions.assertThat(result.writer).isEqualTo("게시글작성자")
        Assertions.assertThat(result.createdAt).isNotNull()
    }

    @Test
    fun `권한이 없는 사용자가 게시글 상세 정보를 조회하면 예외가 발생한다`() {
        //given
        val userId = 6L
        val questionId = 600L
        val writerId = 801L

        val post = createPost(questionId, writerId, "비공개 게시글", "비공개 내용")
        val savedPost = postRepository.save(post)

        given(postPermissionChecker.hasPermission(userId, savedPost.questionId))
            .willReturn(false)

        //when & then
        assertThrows<CoreException> {
            postReader.getPostDetail(userId, savedPost.id)
        }
    }

    private fun createPost(questionId: Long, writerId: Long, title: String, content: String): Post {
        val postContent = PostContent.create(title, content, emptyList())
        return Post.create(questionId, writerId, postContent)
    }

    private fun createPostWithFiles(
        questionId: Long,
        writerId: Long,
        title: String,
        content: String,
        files: List<PostFile>
    ): Post {
        val postContent = PostContent.create(title, content, files)
        return Post.create(questionId, writerId, postContent)
    }
}
