package com.eager.questioncloud.question.product.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.creator.api.internal.CreatorQueryData
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.domain.UserQuestion
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionSortType
import com.eager.questioncloud.question.infrastructure.repository.QuestionMetadataRepository
import com.eager.questioncloud.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.question.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.question.utils.QuestionFixtureHelper
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

@SpringBootTest
@ActiveProfiles("test")
class StoreProductDetailReaderTest(
    @Autowired val storeProductDetailReader: StoreProductDetailReader,
    @Autowired val questionRepository: QuestionRepository,
    @Autowired val userQuestionRepository: UserQuestionRepository,
    @Autowired val questionMetadataRepository: QuestionMetadataRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var creatorQueryAPI: CreatorQueryAPI

    @MockBean
    lateinit var userQueryAPI: UserQueryAPI

    private val userId = 1L
    private val creatorId1 = 101L
    private val creatorId2 = 102L
    private val creatorUserId1 = 201L
    private val creatorUserId2 = 202L

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `문제 목록을 조회할 수 있다`() {
        //given
        val question1 = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId1,
            questionLevel = QuestionLevel.LEVEL3,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )
        val question2 = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId2,
            questionLevel = QuestionLevel.LEVEL4,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )

        saveUserQuestion(userId, question1.id)

        val creatorQueryData1 = CreatorQueryData(creatorUserId1, creatorId1, "수학", 4.5, 100, 50)
        val creatorQueryData2 = CreatorQueryData(creatorUserId2, creatorId2, "영어", 4.8, 80, 30)
        val userQueryData1 = UserQueryData(creatorUserId1, "수학선생님", "math_profile.jpg", "math@test.com")
        val userQueryData2 = UserQueryData(creatorUserId2, "영어선생님", "english_profile.jpg", "english@test.com")

        given(creatorQueryAPI.getCreators(any()))
            .willReturn(listOf(creatorQueryData1, creatorQueryData2))
        given(userQueryAPI.getUsers(any()))
            .willReturn(listOf(userQueryData1, userQueryData2))

        val questionFilter = QuestionFilter(sort = QuestionSortType.Latest)
        val pagingInformation = PagingInformation(0, 10)

        //when
        val storeProductDetails =
            storeProductDetailReader.getStoreProductDetails(userId, questionFilter, pagingInformation)

        //then
        Assertions.assertThat(storeProductDetails).hasSize(2)

        val product1 = storeProductDetails.find { it.questionContent.id == question1.id }
        Assertions.assertThat(product1).isNotNull
        Assertions.assertThat(product1!!.creator).isEqualTo("수학선생님")
        Assertions.assertThat(product1.isOwned).isTrue()

        val product2 = storeProductDetails.find { it.questionContent.id == question2.id }
        Assertions.assertThat(product2).isNotNull
        Assertions.assertThat(product2!!.creator).isEqualTo("영어선생님")
        Assertions.assertThat(product2.isOwned).isFalse()
    }

    @Test
    fun `상품 상세 정보를 조회할 수 있다`() {
        //given
        val question = QuestionFixtureHelper.createQuestion(
            creatorId = creatorId1,
            questionLevel = QuestionLevel.LEVEL3,
            questionRepository = questionRepository,
            questionMetadataRepository = questionMetadataRepository
        )

        saveUserQuestion(userId, question.id)

        val creatorQueryData = CreatorQueryData(creatorUserId1, creatorId1, "수학전문가", 4.7, 200, 100)
        val userQueryData = UserQueryData(creatorUserId1, "김수학", "math_expert.jpg", "math@example.com")

        given(creatorQueryAPI.getCreator(creatorId1))
            .willReturn(creatorQueryData)
        given(userQueryAPI.getUser(creatorUserId1))
            .willReturn(userQueryData)

        //when
        val storeProductDetail = storeProductDetailReader.getStoreProductDetail(question.id, userId)

        //then
        Assertions.assertThat(storeProductDetail.questionContent.id).isEqualTo(question.id)
        Assertions.assertThat(storeProductDetail.creator).isEqualTo("김수학")
        Assertions.assertThat(storeProductDetail.isOwned).isTrue()
    }

    @Test
    fun `필터링된 스토어 상품 개수를 조회할 수 있다`() {
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
                creatorId2,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            ),
            QuestionFixtureHelper.createQuestion(
                creatorId2,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            ),
            QuestionFixtureHelper.createQuestion(
                creatorId2,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
        )

        val questionFilter = QuestionFilter(sort = QuestionSortType.Latest)

        //when
        val count = storeProductDetailReader.count(questionFilter)

        //then
        Assertions.assertThat(count).isEqualTo(5)
    }

    @Test
    fun `특정 크리에이터의 문제만 조회할 수 있다`() {
        //given
        val creator1Questions = listOf(
            QuestionFixtureHelper.createQuestion(
                creatorId1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            ),
            QuestionFixtureHelper.createQuestion(
                creatorId1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
        )
        val creator2Questions = listOf(
            QuestionFixtureHelper.createQuestion(
                creatorId2,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
        )

        val creatorQueryData1 = CreatorQueryData(creatorUserId1, creatorId1, "전문크리에이터", 4.6, 150, 75)
        val userQueryData1 = UserQueryData(creatorUserId1, "이전문", "expert.jpg", "expert@test.com")

        given(creatorQueryAPI.getCreators(any()))
            .willReturn(listOf(creatorQueryData1))
        given(userQueryAPI.getUsers(any()))
            .willReturn(listOf(userQueryData1))

        val questionFilter = QuestionFilter(creatorId = creatorId1, sort = QuestionSortType.Latest)
        val pagingInformation = PagingInformation(0, 10)

        //when
        val storeProductDetails =
            storeProductDetailReader.getStoreProductDetails(userId, questionFilter, pagingInformation)

        //then
        Assertions.assertThat(storeProductDetails).hasSize(2)
        storeProductDetails.forEach { product ->
            Assertions.assertThat(product.questionContent.creatorId).isEqualTo(creatorId1)
            Assertions.assertThat(product.creator).isEqualTo("이전문")
            Assertions.assertThat(product.isOwned).isFalse()
        }
    }

    private fun saveUserQuestion(userId: Long, questionId: Long) {
        val userQuestion = UserQuestion.create(userId, questionId)
        userQuestionRepository.saveAll(listOf(userQuestion))
    }
}
