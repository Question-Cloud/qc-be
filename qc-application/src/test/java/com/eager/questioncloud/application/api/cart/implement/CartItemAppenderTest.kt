package com.eager.questioncloud.application.api.cart.implement

import com.eager.questioncloud.application.utils.Fixture
import com.eager.questioncloud.application.utils.UserFixtureHelper
import com.eager.questioncloud.core.domain.cart.infrastructure.repository.CartItemRepository
import com.eager.questioncloud.core.domain.cart.model.CartItem
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus
import com.eager.questioncloud.core.domain.question.enums.Subject
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class CartItemAppenderTest(
    @Autowired private val cartItemAppender: CartItemAppender,
    @Autowired private val creatorRepository: CreatorRepository,
    @Autowired private val questionRepository: QuestionRepository,
    @Autowired private val cartItemRepository: CartItemRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val userQuestionRepository: UserQuestionRepository,
) {
    private var uid: Long = 0

    @BeforeEach
    fun setUp() {
        uid = UserFixtureHelper.createDefaultEmailUser(userRepository).uid
    }


    @AfterEach
    fun tearDown() {
        questionRepository.deleteAllInBatch()
        cartItemRepository.deleteAllInBatch()
        creatorRepository.deleteAllInBatch()
        userRepository.deleteAllInBatch()
        userQuestionRepository.deleteAllInBatch()
    }

    @Test
    fun `장바구니에 문제를 추가할 수 있다`() {
        //given
        val creator = creatorRepository.save(Creator.create(uid, Subject.Biology, "Hello"))

        val question = questionRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(Question::class.java)
                .set("id", null)
                .set("creatorId", creator.id)
                .set("questionContent.questionCategoryId", 25L)
                .set("questionContent.price", 1000)
                .set("questionStatus", QuestionStatus.Available)
                .sample()
        )

        //when
        cartItemAppender.append(uid, question.id)

        //then
        val isExistInCart = cartItemRepository.isExistsInCart(uid, question.id)
        Assertions.assertThat(isExistInCart).isTrue()
    }

    @Test
    fun `이용 불가능한 문제는 장바구니에 추가할 수 없다`() {
        //given
        val creator = creatorRepository.save(Creator.create(uid, Subject.Biology, "Hello"))

        val unAvailableQuestion = questionRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(Question::class.java)
                .set("id", null)
                .set("creatorId", creator.id)
                .set("questionContent.questionCategoryId", 25L)
                .set("questionContent.price", 1000)
                .set("questionStatus", QuestionStatus.UnAvailable)
                .sample()
        )

        //when then
        Assertions.assertThatThrownBy { cartItemAppender.append(uid, unAvailableQuestion.id) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.UNAVAILABLE_QUESTION)

    }

    @Test
    fun `이미 장바구니에 담긴 문제는 추가할 수 없다`() {
        //given
        val creator = creatorRepository.save(Creator.create(uid, Subject.Biology, "Hello"))

        val question = questionRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(Question::class.java)
                .set("id", null)
                .set("creatorId", creator.id)
                .set("questionContent.questionCategoryId", 25L)
                .set("questionContent.price", 1000)
                .set("questionStatus", QuestionStatus.Available)
                .sample()
        )
        cartItemRepository.save(CartItem.create(uid, question.id))

        //when then
        Assertions.assertThatThrownBy { cartItemAppender.append(uid, question.id) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_IN_CART)
    }

    @Test
    fun `이미 보유하고 있는 문제는 장바구니에 추가할 수 없다`() {
        //given
        val creator = creatorRepository.save(Creator.create(uid, Subject.Biology, "Hello"))

        val question = questionRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(Question::class.java)
                .set("id", null)
                .set("creatorId", creator.id)
                .set("questionContent.questionCategoryId", 25L)
                .set("questionContent.price", 1000)
                .set("questionStatus", QuestionStatus.Available)
                .sample()
        )

        userQuestionRepository.saveAll(UserQuestion.create(uid, listOf(question.id)))

        //when then
        Assertions.assertThatThrownBy { cartItemAppender.append(uid, question.id) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_OWN_QUESTION)
    }
}