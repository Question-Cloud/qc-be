package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.api.CreatorPostQueryAPIResult
import com.eager.questioncloud.post.api.internal.PostQueryAPI
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
class WorkspacePostServiceTest(
    @Autowired
    private val workspacePostService: WorkspacePostService,
    @Autowired
    private val dbCleaner: DBCleaner,
) {
    @MockBean
    private lateinit var questionQueryAPI: QuestionQueryAPI

    @MockBean
    private lateinit var postQueryAPI: PostQueryAPI

    @MockBean
    private lateinit var userQueryAPI: UserQueryAPI

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터가 만든 문제들의 게시글을 조회할 수 있다`() {
        // given
        val creatorId = 1L
        val userId = 100L
        val questionId = 1001L
        val postId = 2001L
        val pagingInformation = PagingInformation(1, 10)

        val questionQueryResult = QuestionInformationQueryResult(
            id = questionId,
            creatorId = creatorId,
            title = "문제 제목",
            subject = "수학",
            parentCategory = "수학",
            childCategory = "미적분",
            thumbnail = "thumb.jpg",
            questionLevel = "LEVEL3",
            price = 1000,
            rate = 4.5
        )

        val creatorPostQueryAPIResult = CreatorPostQueryAPIResult(
            id = postId,
            writerId = userId,
            title = "게시글 제목",
            content = "게시글 내용",
            createdAt = LocalDateTime.now()
        )

        val userQueryData = UserQueryData(
            userId = userId,
            name = "테스트 유저",
            profileImage = "profile.jpg",
            email = "test@test.com"
        )

        given(questionQueryAPI.getCreatorQuestions(creatorId, pagingInformation))
            .willReturn(listOf(questionQueryResult))
        given(postQueryAPI.getCreatorPosts(any(), any()))
            .willReturn(listOf(creatorPostQueryAPIResult))
        given(userQueryAPI.getUsers(any()))
            .willReturn(listOf(userQueryData))

        // when
        val actualPosts = workspacePostService.getCreatorPosts(creatorId, pagingInformation)

        // then
        assertThat(actualPosts).hasSize(1)
        assertThat(actualPosts[0].id).isEqualTo(postId)
        assertThat(actualPosts[0].title).isEqualTo("게시글 제목")
        assertThat(actualPosts[0].writer).isEqualTo("테스트 유저")
    }

    @Test
    fun `크리에이터가 만든 문제들의 게시글 개수를 조회할 수 있다`() {
        // given
        val creatorId = 1L
        val questionId = 1001L
        val expectedCount = 10

        val questionQueryResult = QuestionInformationQueryResult(
            id = questionId,
            creatorId = creatorId,
            title = "문제 제목",
            subject = "수학",
            parentCategory = "수학",
            childCategory = "미적분",
            thumbnail = "thumb.jpg",
            questionLevel = "LEVEL3",
            price = 1000,
            rate = 4.5
        )

        given(questionQueryAPI.getCreatorQuestions(creatorId))
            .willReturn(listOf(questionQueryResult))
        given(postQueryAPI.countByQuestionIdIn(any()))
            .willReturn(expectedCount)

        // when
        val actualCount = workspacePostService.countCreatorPost(creatorId)

        // then
        assertThat(actualCount).isEqualTo(expectedCount)
    }
}