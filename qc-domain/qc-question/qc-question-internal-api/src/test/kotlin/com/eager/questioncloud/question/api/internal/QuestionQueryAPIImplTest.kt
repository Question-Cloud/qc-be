package com.eager.questioncloud.question.api.internal

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.domain.UserQuestion
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionType
import com.eager.questioncloud.question.fixture.QuestionFixtureHelper
import com.eager.questioncloud.question.infrastructure.repository.QuestionMetadataRepository
import com.eager.questioncloud.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.question.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class QuestionQueryAPIImplTest(
    @Autowired val questionQueryAPI: QuestionQueryAPIImpl,
    @Autowired val questionRepository: QuestionRepository,
    @Autowired val userQuestionRepository: UserQuestionRepository,
    @Autowired val questionMetadataRepository: QuestionMetadataRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    private val userId = 1L
    private val creatorId1 = 101L
    private val creatorId2 = 102L

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `문제 정보를 조회할 수 있다`() {
        //given
        val question = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId1,
            questionLevel = QuestionLevel.LEVEL3,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )

        //when
        val result = questionQueryAPI.getQuestionInformation(question.id)

        //then
        Assertions.assertThat(result.id).isEqualTo(question.id)
        Assertions.assertThat(result.creatorId).isEqualTo(creatorId1)
        Assertions.assertThat(result.questionLevel).isEqualTo("LEVEL3")
        Assertions.assertThat(result.price).isEqualTo(1000)
    }

    @Test
    fun `여러 문제 정보를 조회할 수 있다`() {
        //given
        val question1 = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId1,
            questionLevel = QuestionLevel.LEVEL1,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )
        val question2 = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId2,
            questionLevel = QuestionLevel.LEVEL5,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )

        val questionIds = listOf(question1.id, question2.id)

        //when
        val results = questionQueryAPI.getQuestionInformation(questionIds)

        //then
        Assertions.assertThat(results).hasSize(2)

        val result1 = results.find { it.id == question1.id }
        Assertions.assertThat(result1).isNotNull
        Assertions.assertThat(result1!!.creatorId).isEqualTo(creatorId1)
        Assertions.assertThat(result1.questionLevel).isEqualTo("LEVEL1")

        val result2 = results.find { it.id == question2.id }
        Assertions.assertThat(result2).isNotNull
        Assertions.assertThat(result2!!.creatorId).isEqualTo(creatorId2)
        Assertions.assertThat(result2.questionLevel).isEqualTo("LEVEL5")
    }

    @Test
    fun `문제 이용 가능 여부를 확인할 수 있다`() {
        //given
        val question = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId1,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )

        //when
        val isAvailable = questionQueryAPI.isAvailable(question.id)

        //then
        Assertions.assertThat(isAvailable).isTrue()
    }

    @Test
    fun `사용자가 문제를 소유했는지 확인할 수 있다`() {
        //given
        val question = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId1,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )

        saveUserQuestion(userId, question.id)

        //when
        val isOwned = questionQueryAPI.isOwned(userId, question.id)

        //then
        Assertions.assertThat(isOwned).isTrue()
    }

    @Test
    fun `사용자가 문제를 소유하지 않은 경우를 확인할 수 있다`() {
        //given
        val question = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId1,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )

        //when
        val isOwned = questionQueryAPI.isOwned(userId, question.id)

        //then
        Assertions.assertThat(isOwned).isFalse()
    }

    @Test
    fun `사용자가 여러 문제를 모두 소유했는지 확인할 수 있다`() {
        //given
        val question1 = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId1,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )
        val question2 = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId1,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )

        saveUserQuestions(userId, listOf(question1.id, question2.id))

        val questionIds = listOf(question1.id, question2.id)

        //when
        val isOwned = questionQueryAPI.isOwned(userId, questionIds)

        //then
        Assertions.assertThat(isOwned).isTrue()
    }

    @Test
    fun `크리에이터의 문제 목록을 조회할 수 있다`() {
        //given
        val question1 = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId1,
            questionLevel = QuestionLevel.LEVEL2,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )
        val question2 = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId1,
            questionLevel = QuestionLevel.LEVEL4,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )
        val question3 = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId2,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )

        val pagingInformation = PagingInformation(0, 10)

        //when
        val results = questionQueryAPI.getCreatorQuestions(creatorId1, pagingInformation)

        //then
        Assertions.assertThat(results).hasSize(2)
        results.forEach { result ->
            Assertions.assertThat(result.creatorId).isEqualTo(creatorId1)
        }

        val questionIds = results.map { it.id }
        Assertions.assertThat(questionIds).containsExactlyInAnyOrder(question1.id, question2.id)
    }

    @Test
    fun `크리에이터의 문제 개수를 조회할 수 있다`() {
        //given
        val questions = listOf(
            QuestionFixtureHelper.createQuestion(
                creatorId1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            ),
            QuestionFixtureHelper.createQuestion(
                creatorId1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            ),
            QuestionFixtureHelper.createQuestion(
                creatorId1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            ),
            QuestionFixtureHelper.createQuestion(
                creatorId2,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
        )

        //when
        val count = questionQueryAPI.countByCreatorId(creatorId1)

        //then
        Assertions.assertThat(count).isEqualTo(3)
    }

    @Test
    fun `문제 콘텐츠 상세 정보를 조회할 수 있다`() {
        //given
        val question = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId1,
            questionLevel = QuestionLevel.LEVEL3,
            questionType = QuestionType.SelfMade,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )

        //when
        val result = questionQueryAPI.getQuestionContent(question.id, creatorId1)

        //then
        Assertions.assertThat(result.title).isEqualTo(question.questionContent.title)
        Assertions.assertThat(result.description).isEqualTo(question.questionContent.description)
        Assertions.assertThat(result.questionLevel).isEqualTo("LEVEL3")
        Assertions.assertThat(result.price).isEqualTo(1000)
        Assertions.assertThat(result.questionCategoryId).isEqualTo(question.questionContent.questionCategoryId)
    }

    private fun saveUserQuestion(userId: Long, questionId: Long) {
        val userQuestion = UserQuestion.create(userId, questionId)
        userQuestionRepository.saveAll(listOf(userQuestion))
    }

    private fun saveUserQuestions(userId: Long, questionIds: List<Long>) {
        val userQuestions = UserQuestion.create(userId, questionIds)
        userQuestionRepository.saveAll(userQuestions)
    }
}
