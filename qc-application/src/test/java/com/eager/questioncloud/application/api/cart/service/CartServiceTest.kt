package com.eager.questioncloud.application.api.cart.service

import com.eager.questioncloud.application.utils.Fixture
import com.eager.questioncloud.core.domain.cart.infrastructure.repository.CartItemRepository
import com.eager.questioncloud.core.domain.cart.model.CartItem
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class CartServiceTest(
    @Autowired private val cartService: CartService,
    @Autowired private val cartRepository: CartItemRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val questionRepository: QuestionRepository,
    @Autowired private val creatorRepository: CreatorRepository,
) {
    @AfterEach
    fun tearDown() {
        cartRepository.deleteAllInBatch()
        userRepository.deleteAllInBatch()
        questionRepository.deleteAllInBatch()
        creatorRepository.deleteAllInBatch()
    }

    @Test
    fun `장바구니를 조회할 수 있다`() {
        //given
        val user = userRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        val creator = creatorRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                .set(Creator::id, null)
                .set(Creator::userId, user.uid)
                .build()
                .sample()
        )

        val questionIds = Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
            .set(Question::id, null)
            .set(Question::questionStatus, QuestionStatus.Available)
            .set(Question::creatorId, creator.id)
            .sampleList(10)
            .stream()
            .map { question ->
                questionRepository.save(question).id
            }
            .toList()

        questionIds.forEach { questionId -> cartRepository.save(CartItem.create(user.uid, questionId)) }

        // when
        val cartItems = cartService.getCartItems(user.uid)

        //then
        Assertions.assertThat(cartItems).hasSize(10)

        Assertions.assertThat(cartItems.map { it.questionId })
            .containsExactlyInAnyOrderElementsOf(questionIds)
    }

    @Test
    fun `장바구니에 문제를 추가할 수 있다`() {
        //given
        val user = userRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        val creator = creatorRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                .set(Creator::id, null)
                .set(Creator::userId, user.uid)
                .build()
                .sample()
        )

        val question = questionRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
                .set(Question::id, null)
                .set(Question::questionStatus, QuestionStatus.Available)
                .set(Question::creatorId, creator.id)
                .sample()
        )

        // when
        cartService.appendCartItem(user.uid, question.id)

        //then
        val cartItems = cartService.getCartItems(user.uid)
        Assertions.assertThat(cartItems).hasSize(1)
        Assertions.assertThat(cartItems.map { it.questionId })
            .containsExactly(question.id)
    }

    @Test
    fun `장바구니에서 문제를 삭제할 수 있다`() {
        // given
        val user = userRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        val creator = creatorRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                .set(Creator::id, null)
                .set(Creator::userId, user.uid)
                .build()
                .sample()
        )

        val question = questionRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
                .set(Question::id, null)
                .set(Question::questionStatus, QuestionStatus.Available)
                .set(Question::creatorId, creator.id)
                .sample()
        )

        val cartItem = cartRepository.save(CartItem.create(user.uid, question.id))

        // when
        cartService.removeCartItem(listOf(cartItem.id), user.uid)

        // then
        val cartItems = cartService.getCartItems(user.uid)
        Assertions.assertThat(cartItems).isEmpty()
    }
}