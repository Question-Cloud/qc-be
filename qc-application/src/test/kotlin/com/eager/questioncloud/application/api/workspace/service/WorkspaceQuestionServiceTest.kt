package com.eager.questioncloud.application.api.workspace.service

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.QuestionFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.question.enums.QuestionLevel
import com.eager.questioncloud.core.domain.question.enums.QuestionType
import com.eager.questioncloud.core.domain.question.enums.Subject
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.QuestionContent
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewStatisticsRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.exception.CoreException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class WorkspaceQuestionServiceTest(
    @Autowired val workspaceQuestionService: WorkspaceQuestionService,
    @Autowired val questionRepository: QuestionRepository,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val userRepository: UserRepository,
    @Autowired val questionReviewStatisticsRepository: QuestionReviewStatisticsRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터의 문제 목록을 조회할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "creator@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val creator = CreatorFixtureHelper.createCreator(user.uid, creatorRepository)

        repeat(5) {
            val question =
                QuestionFixtureHelper.createQuestion(creatorId = creator.id, questionRepository = questionRepository)
            questionReviewStatisticsRepository.save(QuestionReviewStatistics.create(question.id))
        }

        val pagingInformation = PagingInformation(0, 10)

        // when
        val questions = workspaceQuestionService.getMyQuestions(creator.id, pagingInformation)

        // then
        Assertions.assertThat(questions).hasSize(5)

        questions.forEach { question ->
            Assertions.assertThat(question.title).isNotBlank()
            Assertions.assertThat(question.subject).isNotNull()
            Assertions.assertThat(question.creatorName).isNotBlank()
            Assertions.assertThat(question.questionLevel).isNotNull()
        }
    }

    @Test
    fun `크리에이터의 문제 개수를 조회할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "count@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val creator = CreatorFixtureHelper.createCreator(user.uid, creatorRepository)

        repeat(3) {
            QuestionFixtureHelper.createQuestion(creatorId = creator.id, questionRepository = questionRepository)
        }

        // when
        val count = workspaceQuestionService.countMyQuestions(creator.id)

        // then
        Assertions.assertThat(count).isEqualTo(3)
    }

    @Test
    fun `크리에이터의 특정 문제를 조회할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "content@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val creator = CreatorFixtureHelper.createCreator(user.uid, creatorRepository)
        val question =
            QuestionFixtureHelper.createQuestion(creatorId = creator.id, questionRepository = questionRepository)

        // when
        val questionContent = workspaceQuestionService.getMyQuestionContent(creator.id, question.id)

        // then
        Assertions.assertThat(questionContent).isNotNull()
        Assertions.assertThat(questionContent.questionCategoryId).isEqualTo(question.questionContent.questionCategoryId)
        Assertions.assertThat(questionContent.subject).isEqualTo(question.questionContent.subject)
        Assertions.assertThat(questionContent.title).isEqualTo(question.questionContent.title)
        Assertions.assertThat(questionContent.description).isEqualTo(question.questionContent.description)
        Assertions.assertThat(questionContent.price).isEqualTo(question.questionContent.price)
    }

    @Test
    fun `다른 크리에이터의 문제 내용은 조회할 수 없다`() {
        // given
        val user1 = UserFixtureHelper.createEmailUser(
            "creator1@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val user2 = UserFixtureHelper.createEmailUser(
            "creator2@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )

        val creator1 = CreatorFixtureHelper.createCreator(user1.uid, creatorRepository)
        val creator2 = CreatorFixtureHelper.createCreator(user2.uid, creatorRepository)

        val question =
            QuestionFixtureHelper.createQuestion(creatorId = creator1.id, questionRepository = questionRepository)

        // when & then
        Assertions.assertThatThrownBy {
            workspaceQuestionService.getMyQuestionContent(creator2.id, question.id)
        }.isInstanceOf(CoreException::class.java)
    }

    @Test
    fun `새로운 문제를 등록할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "register@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val creator = CreatorFixtureHelper.createCreator(user.uid, creatorRepository)

        val questionContent = QuestionContent(
            questionCategoryId = 10L,
            subject = Subject.Mathematics,
            title = "새로운 수학 문제",
            description = "이것은 새로운 수학 문제입니다.",
            thumbnail = "thumbnail.jpg",
            fileUrl = "question.pdf",
            explanationUrl = "explanation.pdf",
            questionType = QuestionType.SelfMade,
            questionLevel = QuestionLevel.LEVEL2,
            price = 2000
        )

        // when
        workspaceQuestionService.registerQuestion(creator.id, questionContent)

        // then
        val questions = workspaceQuestionService.getMyQuestions(creator.id, PagingInformation(0, 10))
        Assertions.assertThat(questions).hasSize(1)

        val registeredQuestion = questions[0]
        Assertions.assertThat(registeredQuestion.title).isEqualTo("새로운 수학 문제")
        Assertions.assertThat(registeredQuestion.subject).isEqualTo(Subject.Mathematics)
        Assertions.assertThat(registeredQuestion.price).isEqualTo(2000)

        val reviewStatistics = questionReviewStatisticsRepository.get(registeredQuestion.id)
        Assertions.assertThat(reviewStatistics).isNotNull()
    }

    @Test
    fun `문제를 수정할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "modify@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val creator = CreatorFixtureHelper.createCreator(user.uid, creatorRepository)
        val originalQuestion =
            QuestionFixtureHelper.createQuestion(creatorId = creator.id, questionRepository = questionRepository)

        val modifiedContent = QuestionContent(
            questionCategoryId = 15L,
            subject = Subject.Physics,
            title = "수정된 물리 문제",
            description = "수정된 문제 설명입니다.",
            thumbnail = "new_thumbnail.jpg",
            fileUrl = "new_question.pdf",
            explanationUrl = "new_explanation.pdf",
            questionType = QuestionType.SelfMade,
            questionLevel = QuestionLevel.LEVEL3,
            price = 3000
        )

        // when
        workspaceQuestionService.modifyQuestion(creator.id, originalQuestion.id, modifiedContent)

        // then
        val updatedContent = workspaceQuestionService.getMyQuestionContent(creator.id, originalQuestion.id)

        Assertions.assertThat(updatedContent.title).isEqualTo("수정된 물리 문제")
        Assertions.assertThat(updatedContent.subject).isEqualTo(Subject.Physics)
        Assertions.assertThat(updatedContent.description).isEqualTo("수정된 문제 설명입니다.")
        Assertions.assertThat(updatedContent.price).isEqualTo(3000)
        Assertions.assertThat(updatedContent.questionLevel).isEqualTo(QuestionLevel.LEVEL3)
    }

    @Test
    fun `다른 크리에이터의 문제는 수정할 수 없다`() {
        // given
        val user1 = UserFixtureHelper.createEmailUser(
            "owner@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val user2 = UserFixtureHelper.createEmailUser(
            "other@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )

        val creator1 = CreatorFixtureHelper.createCreator(user1.uid, creatorRepository)
        val creator2 = CreatorFixtureHelper.createCreator(user2.uid, creatorRepository)

        val question =
            QuestionFixtureHelper.createQuestion(creatorId = creator1.id, questionRepository = questionRepository)

        val modifiedContent = QuestionContent(
            questionCategoryId = 15L,
            subject = Subject.Physics,
            title = "수정 시도",
            description = "수정 시도",
            thumbnail = "thumbnail.jpg",
            fileUrl = "file.pdf",
            explanationUrl = "explanation.pdf",
            questionType = QuestionType.SelfMade,
            questionLevel = QuestionLevel.LEVEL1,
            price = 1000
        )

        // when & then
        Assertions.assertThatThrownBy {
            workspaceQuestionService.modifyQuestion(creator2.id, question.id, modifiedContent)
        }.isInstanceOf(CoreException::class.java)
    }

    @Test
    fun `문제를 삭제할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "delete@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val creator = CreatorFixtureHelper.createCreator(user.uid, creatorRepository)
        val question =
            QuestionFixtureHelper.createQuestion(creatorId = creator.id, questionRepository = questionRepository)

        // when
        workspaceQuestionService.deleteQuestion(creator.id, question.id)

        // then
        Assertions.assertThatThrownBy {
            questionRepository.get(question.id)
        }.isInstanceOf(CoreException::class.java)
    }

    @Test
    fun `다른 크리에이터의 문제는 삭제할 수 없다`() {
        // given
        val user1 = UserFixtureHelper.createEmailUser(
            "owner2@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val user2 = UserFixtureHelper.createEmailUser(
            "other2@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )

        val creator1 = CreatorFixtureHelper.createCreator(user1.uid, creatorRepository)
        val creator2 = CreatorFixtureHelper.createCreator(user2.uid, creatorRepository)

        val question =
            QuestionFixtureHelper.createQuestion(creatorId = creator1.id, questionRepository = questionRepository)

        // when & then
        Assertions.assertThatThrownBy {
            workspaceQuestionService.deleteQuestion(creator2.id, question.id)
        }.isInstanceOf(CoreException::class.java)
    }
}
