package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.question.api.internal.*
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class WorkspaceQuestionServiceTest(
    @Autowired
    private val workspaceQuestionService: WorkspaceQuestionService,
    @Autowired
    private val creatorRepository: CreatorRepository,
    @Autowired
    private val dbCleaner: DBCleaner,
) {
    @MockBean
    private lateinit var questionQueryAPI: QuestionQueryAPI

    @MockBean
    private lateinit var questionCommandAPI: QuestionCommandAPI

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터가 만든 문제 목록을 조회할 수 있다`() {
        // given
        val userId = 1L
        val creatorId = 100L
        val pagingInformation = PagingInformation(1, 10)
        val creator = creatorRepository.save(Creator(userId = userId, mainSubject = "수학", introduction = "소개"))

        val expectedQuestions = listOf(
            QuestionInformationQueryResult(
                id = 1L, creatorId = creatorId, title = "문제1", subject = "수학",
                parentCategory = "수학", childCategory = "미적분", thumbnail = "thumb1.jpg",
                questionLevel = "LEVEL3", price = 1000, rate = 4.5
            )
        )
        given(questionQueryAPI.getCreatorQuestions(creator.id, pagingInformation)).willReturn(expectedQuestions)

        // when
        val actualQuestions = workspaceQuestionService.getMyQuestions(userId, pagingInformation)

        // then
        assertThat(actualQuestions).isEqualTo(expectedQuestions)
    }

    @Test
    fun `크리에이터가 만든 문제 목록의 개수를 조회할 수 있다`() {
        // given
        val userId = 1L
        val creator = creatorRepository.save(Creator(userId = userId, mainSubject = "수학", introduction = "소개"))

        val expectedCount = 5
        given(questionQueryAPI.countByCreatorId(creator.id)).willReturn(expectedCount)

        // when
        val actualCount = workspaceQuestionService.countMyQuestions(userId)

        // then
        assertThat(actualCount).isEqualTo(expectedCount)
    }

    @Test
    fun `크리에이터가 만든 문제의 상세 정보를 조회할 수 있다`() {
        // given
        val userId = 1L
        val questionId = 1L
        val creator = creatorRepository.save(Creator(userId = userId, mainSubject = "수학", introduction = "소개"))

        val questionContentQueryResult = QuestionContentQueryResult(
            questionCategoryId = 1L, subject = "수학", title = "문제 제목", description = "설명",
            thumbnail = "thumb.jpg", fileUrl = "file.pdf", explanationUrl = "exp.pdf",
            questionLevel = "LEVEL3", price = 1000
        )
        given(questionQueryAPI.getQuestionContent(questionId, creator.id)).willReturn(questionContentQueryResult)

        // when
        val actualContent = workspaceQuestionService.getMyQuestionContent(userId, questionId)

        // then
        assertThat(actualContent.title).isEqualTo(questionContentQueryResult.title)
        assertThat(actualContent.subject).isEqualTo(questionContentQueryResult.subject)
        assertThat(actualContent.questionCategoryId).isEqualTo(questionContentQueryResult.questionCategoryId)
        assertThat(actualContent.description).isEqualTo(questionContentQueryResult.description)
        assertThat(actualContent.thumbnail).isEqualTo(questionContentQueryResult.thumbnail)
        assertThat(actualContent.fileUrl).isEqualTo(questionContentQueryResult.fileUrl)
        assertThat(actualContent.explanationUrl).isEqualTo(questionContentQueryResult.explanationUrl)
        assertThat(actualContent.questionLevel).isEqualTo(questionContentQueryResult.questionLevel)
        assertThat(actualContent.price).isEqualTo(questionContentQueryResult.price)
    }

    @Test
    fun `문제를 등록할 수 있다`() {
        // given
        val userId = 1L
        val creator = creatorRepository.save(Creator(userId = userId, mainSubject = "수학", introduction = "소개"))

        val command = RegisterQuestionCommand(
            questionCategoryId = 1L, subject = "수학", title = "새 문제", description = "새 설명",
            thumbnail = "new_thumb.jpg", fileUrl = "new_file.pdf", explanationUrl = "new_exp.pdf",
            questionLevel = "LEVEL3", price = 1000
        )

        // when
        workspaceQuestionService.registerQuestion(userId, command)

        // then
        verify(questionCommandAPI).register(creator.id, command)
    }

    @Test
    fun `문제를 수정할 수 있다`() {
        // given
        val userId = 1L
        val questionId = 1L
        val creator = creatorRepository.save(Creator(userId = userId, mainSubject = "수학", introduction = "소개"))

        val command = ModifyQuestionCommand(
            questionCategoryId = 1L, subject = "수학", title = "수정된 문제",
            description = "수정된 설명", thumbnail = "modified_thumb.jpg", fileUrl = "modified_file.pdf",
            explanationUrl = "modified_exp.pdf", questionLevel = "LEVEL4", price = 1500
        )

        // when
        workspaceQuestionService.modifyQuestion(userId, questionId, command)

        // then
        verify(questionCommandAPI).modify(questionId, command)
    }

    @Test
    fun `문제를 삭제할 수 있다`() {
        // given
        val userId = 1L
        val questionId = 1L
        val creator = creatorRepository.save(Creator(userId = userId, mainSubject = "수학", introduction = "소개"))

        // when
        workspaceQuestionService.deleteQuestion(userId, questionId)

        // then
        verify(questionCommandAPI).delete(questionId, creator.id)
    }
}