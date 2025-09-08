package com.eager.questioncloud.cart.implement

import com.eager.questioncloud.cart.domain.CartItem
import com.eager.questioncloud.cart.infrastructure.repository.CartItemRepository
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class CartItemAppenderTest(
    @Autowired val cartItemAppender: CartItemAppender,
    @Autowired val cartItemRepository: CartItemRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var questionQueryAPI: QuestionQueryAPI
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `장바구니에 문제를 추가할 수 있다`() {
        //given
        val userId = 100L
        val questionId = 200L
        
        given(questionQueryAPI.isAvailable(questionId))
            .willReturn(true)
        
        given(questionQueryAPI.isOwned(userId, questionId))
            .willReturn(false)
        
        //when
        cartItemAppender.append(userId, questionId)
        
        //then
        val cartItems = cartItemRepository.findByUserId(userId)
        Assertions.assertThat(cartItems).hasSize(1)
        
        val cartItem = cartItems[0]
        Assertions.assertThat(cartItem.userId).isEqualTo(userId)
        Assertions.assertThat(cartItem.questionId).isEqualTo(questionId)
    }
    
    @Test
    fun `이용할 수 없는 문제는 장바구니에 추가할 수 없다`() {
        //given
        val userId = 101L
        val questionId = 201L
        
        given(questionQueryAPI.isAvailable(questionId))
            .willReturn(false)
        
        //when & then
        assertThrows<CoreException> {
            cartItemAppender.append(userId, questionId)
        }
        
        val cartItems = cartItemRepository.findByUserId(userId)
        Assertions.assertThat(cartItems).isEmpty()
    }
    
    @Test
    fun `이미 장바구니에 있는 문제는 추가할 수 없다`() {
        //given
        val userId = 102L
        val questionId = 202L
        
        val existingCartItem = CartItem.create(userId, questionId)
        cartItemRepository.save(existingCartItem)
        
        given(questionQueryAPI.isAvailable(questionId))
            .willReturn(true)
        
        //when & then
        assertThrows<CoreException> {
            cartItemAppender.append(userId, questionId)
        }
        
        val cartItems = cartItemRepository.findByUserId(userId)
        Assertions.assertThat(cartItems).hasSize(1)
    }
    
    @Test
    fun `이미 소유한 문제는 장바구니에 추가할 수 없다`() {
        //given
        val userId = 103L
        val questionId = 203L
        
        given(questionQueryAPI.isAvailable(questionId))
            .willReturn(true)
        
        given(questionQueryAPI.isOwned(userId, questionId))
            .willReturn(true)
        
        //when & then
        assertThrows<CoreException> {
            cartItemAppender.append(userId, questionId)
        }
        
        val cartItems = cartItemRepository.findByUserId(userId)
        Assertions.assertThat(cartItems).isEmpty()
    }
}
