package com.eager.questioncloud.post.implement

import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.creator.api.internal.CreatorQueryData
import com.eager.questioncloud.post.domain.Post
import com.eager.questioncloud.post.domain.PostContent
import com.eager.questioncloud.post.infrastructure.repository.PostRepository
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class PostPermissionCheckerTest(
    @Autowired val postPermissionChecker: PostPermissionChecker,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var questionQueryAPI: QuestionQueryAPI

    @MockBean
    lateinit var creatorQueryAPI: CreatorQueryAPI

    @MockBean
    lateinit var postRepository: PostRepository

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터는 문제에 대한 권한을 가진다`() {
        //given
        val userId = 1L
        val questionId = 100L
        val creatorId = 301L
        val creatorUserId = 401L

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

        given(questionQueryAPI.getQuestionInformation(questionId))
            .willReturn(questionInformation)

        val creatorQueryData = CreatorQueryData(
            userId = creatorUserId,
            creatorId = creatorId,
            mainSubject = "수학",
            rate = 4.5,
            sales = 100,
            subscriberCount = 500
        )
        given(creatorQueryAPI.getCreator(creatorId))
            .willReturn(creatorQueryData)

        //when
        val hasPermission = postPermissionChecker.hasPermission(creatorUserId, questionId)

        //then
        Assertions.assertThat(hasPermission).isTrue()
    }

    @Test
    fun `문제를 소유한 사용자는 권한을 가진다`() {
        //given
        val userId = 2L
        val questionId = 200L
        val creatorId = 302L
        val creatorUserId = 402L

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
        given(questionQueryAPI.getQuestionInformation(questionId))
            .willReturn(questionInformation)

        val creatorQueryData = CreatorQueryData(
            userId = creatorUserId,
            creatorId = creatorId,
            mainSubject = "수학",
            rate = 4.5,
            sales = 100,
            subscriberCount = 500
        )
        given(creatorQueryAPI.getCreator(creatorId))
            .willReturn(creatorQueryData)

        given(questionQueryAPI.isOwned(userId, questionId))
            .willReturn(true)

        //when
        val hasPermission = postPermissionChecker.hasPermission(userId, questionId)

        //then
        Assertions.assertThat(hasPermission).isTrue()
    }

    @Test
    fun `크리에이터도 아니고 문제를 소유하지도 않은 사용자는 권한이 없다`() {
        //given
        val userId = 3L
        val questionId = 300L
        val creatorId = 303L
        val creatorUserId = 403L

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
        given(questionQueryAPI.getQuestionInformation(questionId))
            .willReturn(questionInformation)

        val creatorQueryData = CreatorQueryData(
            userId = creatorUserId,
            creatorId = creatorId,
            mainSubject = "수학",
            rate = 4.5,
            sales = 100,
            subscriberCount = 500
        )
        given(creatorQueryAPI.getCreator(creatorId))
            .willReturn(creatorQueryData)

        given(questionQueryAPI.isOwned(userId, questionId))
            .willReturn(false)

        //when
        val hasPermission = postPermissionChecker.hasPermission(userId, questionId)

        //then
        Assertions.assertThat(hasPermission).isFalse()
    }

    @Test
    fun `댓글 권한 확인 - 해당 포스트의 문제에 대한 권한이 있으면 댓글 권한도 있다`() {
        //given
        val userId = 4L
        val questionId = 400L
        val postId = 500L
        val creatorId = 304L
        val creatorUserId = 404L

        val post = createMockPost(questionId, userId, "테스트 포스트")
        given(postRepository.findById(postId))
            .willReturn(post)

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
        given(questionQueryAPI.getQuestionInformation(questionId))
            .willReturn(questionInformation)

        val creatorQueryData = CreatorQueryData(
            userId = creatorUserId,
            creatorId = creatorId,
            mainSubject = "수학",
            rate = 4.5,
            sales = 100,
            subscriberCount = 500
        )
        given(creatorQueryAPI.getCreator(creatorId))
            .willReturn(creatorQueryData)

        //when
        val hasCommentPermission = postPermissionChecker.hasCommentPermission(creatorUserId, postId)

        //then
        Assertions.assertThat(hasCommentPermission).isTrue()
    }

    @Test
    fun `댓글 권한 확인 - 해당 포스트의 문제에 대한 권한이 없으면 댓글 권한도 없다`() {
        //given
        val userId = 5L
        val questionId = 500L
        val postId = 600L
        val creatorId = 305L
        val creatorUserId = 405L

        val post = createMockPost(questionId, userId, "테스트 포스트")
        given(postRepository.findById(postId))
            .willReturn(post)

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
        given(questionQueryAPI.getQuestionInformation(questionId))
            .willReturn(questionInformation)

        val creatorQueryData = CreatorQueryData(
            userId = creatorUserId,
            creatorId = creatorId,
            mainSubject = "수학",
            rate = 4.5,
            sales = 100,
            subscriberCount = 500
        )
        given(creatorQueryAPI.getCreator(creatorId))
            .willReturn(creatorQueryData)

        given(questionQueryAPI.isOwned(userId, questionId))
            .willReturn(false)

        //when
        val hasCommentPermission = postPermissionChecker.hasCommentPermission(userId, postId)

        //then
        Assertions.assertThat(hasCommentPermission).isFalse()
    }

    @Test
    fun `isCreator - 크리에이터 확인이 정확하게 동작한다`() {
        //given
        val userId = 6L
        val questionId = 600L
        val creatorId = 306L
        val creatorUserId = 406L

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
        given(questionQueryAPI.getQuestionInformation(questionId))
            .willReturn(questionInformation)

        val creatorQueryData = CreatorQueryData(
            userId = creatorUserId,
            creatorId = creatorId,
            mainSubject = "수학",
            rate = 4.5,
            sales = 100,
            subscriberCount = 500
        )
        given(creatorQueryAPI.getCreator(creatorId))
            .willReturn(creatorQueryData)

        //when
        val isCreator = postPermissionChecker.isCreator(creatorUserId, questionId)
        val isNotCreator = postPermissionChecker.isCreator(userId, questionId)

        //then
        Assertions.assertThat(isCreator).isTrue()
        Assertions.assertThat(isNotCreator).isFalse()
    }

    @Test
    fun `크리에이터는 문제 소유 여부와 관계없이 권한을 가진다`() {
        //given
        val questionId = 700L
        val creatorId = 307L
        val creatorUserId = 407L

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
        given(questionQueryAPI.getQuestionInformation(questionId))
            .willReturn(questionInformation)

        val creatorQueryData = CreatorQueryData(
            userId = creatorUserId,
            creatorId = creatorId,
            mainSubject = "수학",
            rate = 4.5,
            sales = 100,
            subscriberCount = 500
        )
        given(creatorQueryAPI.getCreator(creatorId))
            .willReturn(creatorQueryData)

        given(questionQueryAPI.isOwned(creatorUserId, questionId))
            .willReturn(false)

        //when
        val hasPermission = postPermissionChecker.hasPermission(creatorUserId, questionId)

        //then
        Assertions.assertThat(hasPermission).isTrue()
    }

    private fun createMockPost(questionId: Long, writerId: Long, content: String): Post {
        val postContent = PostContent.create("테스트 제목", content, emptyList())
        return Post.create(questionId, writerId, postContent)
    }
}
