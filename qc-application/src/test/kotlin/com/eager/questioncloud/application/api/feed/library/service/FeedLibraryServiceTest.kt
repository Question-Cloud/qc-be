package com.eager.questioncloud.application.api.feed.library.service

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.QuestionFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import com.eager.questioncloud.core.domain.question.enums.QuestionLevel
import com.eager.questioncloud.core.domain.question.enums.QuestionSortType
import com.eager.questioncloud.core.domain.question.enums.QuestionType
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Collectors

@SpringBootTest
@ActiveProfiles("test")
class FeedLibraryServiceTest(
    @Autowired private val feedLibraryService: FeedLibraryService,
    @Autowired private val userQuestionRepository: UserQuestionRepository,
    @Autowired private val questionRepository: QuestionRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val creatorRepository: CreatorRepository,
    @Autowired private val dbCleaner: DBCleaner,
) {
    private var creatorId: Long = 0

    @BeforeEach
    fun setUp() {
        val creatorUser = UserFixtureHelper.createDefaultEmailUser(userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        creatorId = creator.id
    }

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `구매한 문제를 조회할 수 있다`() {
        // given
        val user = UserFixtureHelper.createDefaultEmailUser(userRepository)
        val targetCategory = 25L
        val targetLevel = QuestionLevel.LEVEL2
        val targetType = QuestionType.SelfMade
        val sortType = QuestionSortType.Rate
        val pagingInformation = PagingInformation(0, 100000)
        val questionFilter = QuestionFilter(
            userId = user.uid,
            categories = listOf(targetCategory),
            levels = listOf(targetLevel),
            questionType = targetType,
            sort = sortType,
            pagingInformation = pagingInformation
        )

        val randomDummyQuestions = dummyQuestions(30)
        val targetDummyQuestions = dummyQuestions(10, targetCategory, targetLevel)
        userQuestionRepository.saveAll(
            UserQuestion.create(
                user.uid, randomDummyQuestions.stream().map { it.id }.collect(
                    Collectors.toList()
                )
            )
        )
        userQuestionRepository.saveAll(
            UserQuestion.create(
                user.uid, targetDummyQuestions.stream().map { it.id }.collect(
                    Collectors.toList()
                )
            )
        )

        // when
        val result = feedLibraryService.getUserQuestions(questionFilter)

        // then
        Assertions.assertThat(result.size).isEqualTo(targetDummyQuestions.size)
    }

    private fun dummyQuestions(count: Int): List<Question> {
        val questions: MutableList<Question> = mutableListOf()
        for (i in 1..count) {
            questions.add(
                QuestionFixtureHelper.createQuestion(creatorId = creatorId, questionRepository = questionRepository)
            )
        }
        return questions
    }

    private fun dummyQuestions(count: Int, category: Long, questionLevel: QuestionLevel): List<Question> {
        val questions: MutableList<Question> = mutableListOf()
        for (i in 1..count) {
            questions.add(
                QuestionFixtureHelper.createQuestion(
                    creatorId = creatorId,
                    category = category,
                    questionLevel = questionLevel,
                    questionRepository = questionRepository
                )
            )
        }
        return questions
    }
}