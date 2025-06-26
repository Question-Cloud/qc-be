package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.api.CreatorPostQueryAPIResult
import com.eager.questioncloud.post.api.internal.PostQueryAPI
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
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
class WorkspacePostReaderTest(
    @Autowired val workspacePostReader: WorkspacePostReader,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var questionQueryAPI: QuestionQueryAPI

    @MockBean
    lateinit var postQueryAPI: PostQueryAPI

    @MockBean
    lateinit var userQueryAPI: UserQueryAPI

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터의 게시글 목록을 조회할 수 있다`() {
        //given
        val creatorId = 1L
        val userId = 100L
        val questionId1 = 1001L
        val questionId2 = 1002L
        val postId1 = 2001L
        val postId2 = 2002L

        val pagingInformation = PagingInformation(0, 10)

        val question1 = QuestionInformationQueryResult(
            id = questionId1,
            creatorId = creatorId,
            title = "문제1",
            subject = "수학",
            parentCategory = "수학",
            childCategory = "미적분",
            thumbnail = "thumb1.jpg",
            questionLevel = "LEVEL3",
            price = 1000,
            rate = 4.5
        )
        val question2 = QuestionInformationQueryResult(
            id = questionId2,
            creatorId = creatorId,
            title = "문제2",
            subject = "수학",
            parentCategory = "수학",
            childCategory = "확률과 통계",
            thumbnail = "thumb2.jpg",
            questionLevel = "LEVEL4",
            price = 1500,
            rate = 4.0
        )

        given(questionQueryAPI.getCreatorQuestions(creatorId, pagingInformation))
            .willReturn(listOf(question1, question2))

        val post1 = CreatorPostQueryAPIResult(
            id = postId1,
            writerId = userId,
            title = "게시글1",
            content = "게시글 내용1",
            createdAt = LocalDateTime.now().minusDays(1)
        )
        val post2 = CreatorPostQueryAPIResult(
            id = postId2,
            writerId = userId,
            title = "게시글2",
            content = "게시글 내용2",
            createdAt = LocalDateTime.now()
        )

        given(postQueryAPI.getCreatorPosts(any(), any()))
            .willReturn(listOf(post1, post2))

        val user = UserQueryData(
            userId = userId,
            name = "테스트 유저",
            profileImage = "user_profile.jpg",
            email = "user@test.com"
        )

        given(userQueryAPI.getUsers(any()))
            .willReturn(listOf(user))

        //when
        val result = workspacePostReader.getCreatorPosts(creatorId, pagingInformation)

        //then
        Assertions.assertThat(result).hasSize(2)
        Assertions.assertThat(result[0].id).isEqualTo(postId1)
        Assertions.assertThat(result[0].title).isEqualTo("게시글1")
        Assertions.assertThat(result[0].writer).isEqualTo("테스트 유저")
        Assertions.assertThat(result[1].id).isEqualTo(postId2)
        Assertions.assertThat(result[1].title).isEqualTo("게시글2")
        Assertions.assertThat(result[1].writer).isEqualTo("테스트 유저")
    }

    @Test
    fun `크리에이터의 게시글 개수를 조회할 수 있다`() {
        //given
        val creatorId = 1L
        val questionId1 = 1001L
        val questionId2 = 1002L

        val question1 = QuestionInformationQueryResult(
            id = questionId1,
            creatorId = creatorId,
            title = "문제1",
            subject = "수학",
            parentCategory = "수학",
            childCategory = "미적분",
            thumbnail = "thumb1.jpg",
            questionLevel = "LEVEL3",
            price = 1000,
            rate = 4.5
        )
        val question2 = QuestionInformationQueryResult(
            id = questionId2,
            creatorId = creatorId,
            title = "문제2",
            subject = "수학",
            parentCategory = "수학",
            childCategory = "확률과 통계",
            thumbnail = "thumb2.jpg",
            questionLevel = "LEVEL4",
            price = 1500,
            rate = 4.0
        )

        given(questionQueryAPI.getCreatorQuestions(creatorId, PagingInformation.max))
            .willReturn(listOf(question1, question2))

        given(postQueryAPI.countByQuestionIdIn(any()))
            .willReturn(5)

        //when
        val result = workspacePostReader.countCreatorPost(creatorId)

        //then
        Assertions.assertThat(result).isEqualTo(5)
    }
}