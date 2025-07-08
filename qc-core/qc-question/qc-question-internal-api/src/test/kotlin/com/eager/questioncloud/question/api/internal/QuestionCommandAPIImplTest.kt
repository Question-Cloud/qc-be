package com.eager.questioncloud.question.api.internal

import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionStatus
import com.eager.questioncloud.question.enums.Subject
import com.eager.questioncloud.question.fixture.QuestionFixtureHelper
import com.eager.questioncloud.question.infrastructure.repository.QuestionMetadataRepository
import com.eager.questioncloud.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class QuestionCommandAPIImplTest(
    @Autowired val questionCommandAPI: QuestionCommandAPIImpl,
    @Autowired val questionRepository: QuestionRepository,
    @Autowired val questionMetadataRepository: QuestionMetadataRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    private val creatorId = 101L

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `새로운 문제를 등록할 수 있다`() {
        //given
        val registerCommand = RegisterQuestionCommand(
            questionCategoryId = 1L,
            subject = "Mathematics",
            title = "수학 문제 제목",
            description = "수학 문제 설명",
            thumbnail = "thumbnail.jpg",
            fileUrl = "question.pdf",
            explanationUrl = "explanation.pdf",
            questionLevel = "LEVEL3",
            price = 2000
        )

        //when
        val newQuestionId = questionCommandAPI.register(creatorId, registerCommand)

        //then
        val createdQuestion = questionRepository.get(newQuestionId)

        Assertions.assertThat(createdQuestion.creatorId).isEqualTo(creatorId)
        Assertions.assertThat(createdQuestion.questionContent.title).isEqualTo("수학 문제 제목")
        Assertions.assertThat(createdQuestion.questionContent.description).isEqualTo("수학 문제 설명")
        Assertions.assertThat(createdQuestion.questionContent.subject).isEqualTo(Subject.Mathematics)
        Assertions.assertThat(createdQuestion.questionContent.questionLevel).isEqualTo(QuestionLevel.LEVEL3)
        Assertions.assertThat(createdQuestion.questionContent.price).isEqualTo(2000)
        Assertions.assertThat(createdQuestion.questionStatus).isEqualTo(QuestionStatus.Available)

        val questionMetadata = questionMetadataRepository.getForUpdate(createdQuestion.id)
        Assertions.assertThat(questionMetadata.questionId).isEqualTo(createdQuestion.id)
        Assertions.assertThat(questionMetadata.sales).isEqualTo(0)
        Assertions.assertThat(questionMetadata.reviewCount).isEqualTo(0)
        Assertions.assertThat(questionMetadata.reviewAverageRate).isEqualTo(0.0)
    }

    @Test
    fun `기존 문제를 수정할 수 있다`() {
        //given
        val existingQuestion = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId,
            questionLevel = QuestionLevel.LEVEL1,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )

        val modifyCommand = ModifyQuestionCommand(
            questionCategoryId = 2L,
            subject = "Physics",
            title = "수정된 물리 문제",
            description = "수정된 물리 문제 설명",
            thumbnail = "modified_thumbnail.jpg",
            fileUrl = "modified_question.pdf",
            explanationUrl = "modified_explanation.pdf",
            questionLevel = "LEVEL5",
            price = 3000
        )

        //when
        questionCommandAPI.modify(existingQuestion.id, modifyCommand)

        //then
        val modifiedQuestion = questionRepository.get(existingQuestion.id)
        Assertions.assertThat(modifiedQuestion.id).isEqualTo(existingQuestion.id)
        Assertions.assertThat(modifiedQuestion.creatorId).isEqualTo(creatorId) // 변경되지 않음
        Assertions.assertThat(modifiedQuestion.questionContent.questionCategoryId).isEqualTo(2L)
        Assertions.assertThat(modifiedQuestion.questionContent.subject).isEqualTo(Subject.Physics)
        Assertions.assertThat(modifiedQuestion.questionContent.title).isEqualTo("수정된 물리 문제")
        Assertions.assertThat(modifiedQuestion.questionContent.description).isEqualTo("수정된 물리 문제 설명")
        Assertions.assertThat(modifiedQuestion.questionContent.thumbnail).isEqualTo("modified_thumbnail.jpg")
        Assertions.assertThat(modifiedQuestion.questionContent.fileUrl).isEqualTo("modified_question.pdf")
        Assertions.assertThat(modifiedQuestion.questionContent.explanationUrl).isEqualTo("modified_explanation.pdf")
        Assertions.assertThat(modifiedQuestion.questionContent.questionLevel).isEqualTo(QuestionLevel.LEVEL5)
        Assertions.assertThat(modifiedQuestion.questionContent.price).isEqualTo(3000)
    }

    @Test
    fun `문제를 삭제할 수 있다`() {
        //given
        val existingQuestion = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )

        Assertions.assertThat(existingQuestion.questionStatus).isEqualTo(QuestionStatus.Available)

        //when
        questionCommandAPI.delete(existingQuestion.id, creatorId)

        //then
        Assertions.assertThatThrownBy {
            questionRepository.get(existingQuestion.id)
        }.hasFieldOrPropertyWithValue("error", Error.UNAVAILABLE_QUESTION)
    }
}
