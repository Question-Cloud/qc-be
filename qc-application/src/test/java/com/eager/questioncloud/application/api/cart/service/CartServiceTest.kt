package com.eager.questioncloud.application.api.cart.service

import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.QuestionFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.cart.infrastructure.repository.CartItemRepository
import com.eager.questioncloud.core.domain.cart.model.CartItem
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
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
    private var uid: Long = 0
    private var creatorId: Long = 0

    @BeforeEach
    fun setUp() {
        uid = UserFixtureHelper.createDefaultEmailUser(userRepository).uid
        creatorId = CreatorFixtureHelper.createCreator(uid, creatorRepository).id
    }

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
        val questionIds = dummyQuestionIds()

        questionIds.forEach { questionId -> cartRepository.save(CartItem.create(uid, questionId)) }

        // when
        val cartItems = cartService.getCartItems(uid)

        //then
        Assertions.assertThat(cartItems).hasSize(10)

        Assertions.assertThat(cartItems.map { it.questionId })
            .containsExactlyInAnyOrderElementsOf(questionIds)
    }

    @Test
    fun `장바구니에 문제를 추가할 수 있다`() {
        //given
        val question = createDummyQuestion()

        // when
        cartService.appendCartItem(uid, question.id)

        //then
        val cartItems = cartService.getCartItems(uid)
        Assertions.assertThat(cartItems).hasSize(1)
        Assertions.assertThat(cartItems.map { it.questionId })
            .containsExactly(question.id)
    }

    @Test
    fun `장바구니에서 문제를 삭제할 수 있다`() {
        // given
        val question = createDummyQuestion()
        val cartItem = cartRepository.save(CartItem.create(uid, question.id))

        // when
        cartService.removeCartItem(listOf(cartItem.id), uid)

        // then
        val cartItems = cartService.getCartItems(uid)
        Assertions.assertThat(cartItems).isEmpty()
    }

    private fun createDummyQuestion(): Question {
        return QuestionFixtureHelper.createQuestion(creatorId = creatorId, questionRepository = questionRepository)
    }

    private fun dummyQuestionIds(): List<Long> {
        val questionIds = mutableListOf<Long>()

        for (i in 1..10) {
            val question = createDummyQuestion()
            questionIds.add(question.id)
        }

        return questionIds
    }
}