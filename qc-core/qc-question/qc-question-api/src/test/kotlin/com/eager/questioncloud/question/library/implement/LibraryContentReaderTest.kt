package com.eager.questioncloud.question.library.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.creator.api.internal.CreatorQueryData
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.domain.UserQuestion
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionSortType
import com.eager.questioncloud.question.enums.QuestionType
import com.eager.questioncloud.question.fixture.QuestionFixtureHelper
import com.eager.questioncloud.question.repository.QuestionMetadataRepository
import com.eager.questioncloud.question.repository.QuestionRepository
import com.eager.questioncloud.question.repository.UserQuestionRepository
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.test.utils.DBCleaner
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
class LibraryContentReaderTest(
    @Autowired val libraryContentReader: LibraryContentReader,
    @Autowired val userQuestionRepository: UserQuestionRepository,
    @Autowired val questionRepository: QuestionRepository,
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
    fun `사용자의 문제 목록을 조회할 수 있다`() {
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
        
        saveUserQuestions(userId, listOf(question1.id, question2.id))
        
        val creatorQueryData1 = CreatorQueryData(creatorUserId1, creatorId1, "수학", "zzzz", 4.5, 100, 50)
        val creatorQueryData2 = CreatorQueryData(creatorUserId2, creatorId2, "영어", "zzzz", 4.8, 80, 30)
        val userQueryData1 = UserQueryData(creatorUserId1, "수학선생님", "math_profile.jpg", "math@test.com")
        val userQueryData2 = UserQueryData(creatorUserId2, "영어선생님", "english_profile.jpg", "english@test.com")
        
        given(creatorQueryAPI.getCreators(any()))
            .willReturn(listOf(creatorQueryData1, creatorQueryData2))
        given(userQueryAPI.getUsers(any()))
            .willReturn(listOf(userQueryData1, userQueryData2))
        
        val questionFilter = QuestionFilter(sort = QuestionSortType.Latest)
        val pagingInformation = PagingInformation(0, 10)
        
        //when
        val libraryContents = libraryContentReader.getUserQuestions(userId, questionFilter, pagingInformation)
        
        //then
        Assertions.assertThat(libraryContents).hasSize(2)
        
        val creatorIds = libraryContents.map { it.content.creatorId }
        Assertions.assertThat(creatorIds).containsExactlyInAnyOrder(creatorId1, creatorId2)
        
        val creator1Content = libraryContents.find { it.content.creatorId == creatorId1 }
        Assertions.assertThat(creator1Content!!.creator.name).isEqualTo("수학선생님")
        Assertions.assertThat(creator1Content.creator.mainSubject).isEqualTo("수학")
        
        val creator2Content = libraryContents.find { it.content.creatorId == creatorId2 }
        Assertions.assertThat(creator2Content!!.creator.name).isEqualTo("영어선생님")
        Assertions.assertThat(creator2Content.creator.mainSubject).isEqualTo("영어")
    }
    
    @Test
    fun `사용자의 문제 개수를 조회할 수 있다`() {
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
        
        saveUserQuestions(userId, questions.map { it.id })
        
        val questionFilter = QuestionFilter(sort = QuestionSortType.Latest)
        
        //when
        val count = libraryContentReader.countUserQuestions(userId, questionFilter)
        
        //then
        Assertions.assertThat(count).isEqualTo(5)
    }
    
    @Test
    fun `특정 크리에이터의 문제만 필터링하여 조회할 수 있다`() {
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
            ),
            QuestionFixtureHelper.createQuestion(
                creatorId2,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
        )
        
        val allQuestions = creator1Questions + creator2Questions
        saveUserQuestions(userId, allQuestions.map { it.id })
        
        val creatorQueryData1 = CreatorQueryData(creatorUserId1, creatorId1, "수학선생님", "zzzz", 4.5, 100, 50)
        val userQueryData1 = UserQueryData(creatorUserId1, "김수학", "math_teacher.jpg", "math@example.com")
        
        given(creatorQueryAPI.getCreators(any()))
            .willReturn(listOf(creatorQueryData1))
        given(userQueryAPI.getUsers(any()))
            .willReturn(listOf(userQueryData1))
        
        val questionFilter = QuestionFilter(creatorId = creatorId1, sort = QuestionSortType.Latest)
        val pagingInformation = PagingInformation(0, 10)
        
        //when
        val libraryContents = libraryContentReader.getUserQuestions(userId, questionFilter, pagingInformation)
        
        //then
        Assertions.assertThat(libraryContents).hasSize(3)
        libraryContents.forEach { content ->
            Assertions.assertThat(content.content.creatorId).isEqualTo(creatorId1)
            Assertions.assertThat(content.creator.name).isEqualTo("김수학")
        }
    }
    
    @Test
    fun `특정 난이도의 문제만 필터링하여 조회할 수 있다`() {
        //given
        val easyQuestions = listOf(
            QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionLevel = QuestionLevel.LEVEL1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            ),
            QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionLevel = QuestionLevel.LEVEL1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
        )
        val mediumQuestions = listOf(
            QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionLevel = QuestionLevel.LEVEL3,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
        )
        val hardQuestions = listOf(
            QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionLevel = QuestionLevel.LEVEL5,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
        )
        
        val allQuestions = easyQuestions + mediumQuestions + hardQuestions
        saveUserQuestions(userId, allQuestions.map { it.id })
        
        val creatorQueryData = CreatorQueryData(creatorUserId1, creatorId1, "난이도전문가", "zzzz", 4.7, 200, 100)
        val userQueryData = UserQueryData(creatorUserId1, "박난이도", "level_expert.jpg", "level@test.com")
        
        given(creatorQueryAPI.getCreators(any()))
            .willReturn(listOf(creatorQueryData))
        given(userQueryAPI.getUsers(any()))
            .willReturn(listOf(userQueryData))
        
        val questionFilter = QuestionFilter(
            levels = listOf(QuestionLevel.LEVEL1, QuestionLevel.LEVEL3),
            sort = QuestionSortType.Latest
        )
        val pagingInformation = PagingInformation(0, 10)
        
        //when
        val libraryContents = libraryContentReader.getUserQuestions(userId, questionFilter, pagingInformation)
        
        //then
        Assertions.assertThat(libraryContents).hasSize(3)
        val levels = libraryContents.map { it.content.questionLevel }
        Assertions.assertThat(levels).containsOnly(QuestionLevel.LEVEL1, QuestionLevel.LEVEL3)
        libraryContents.forEach { content ->
            Assertions.assertThat(content.creator.name).isEqualTo("박난이도")
        }
    }
    
    @Test
    fun `특정 카테고리의 문제만 필터링하여 조회할 수 있다`() {
        //given
        val category1 = 10L
        val category2 = 20L
        val category3 = 30L
        
        val category1Questions = listOf(
            QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                category = category1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            ),
            QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                category = category1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
        )
        val category2Questions = listOf(
            QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                category = category2,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
        )
        val category3Questions = listOf(
            QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                category = category3,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
        )
        
        val allQuestions = category1Questions + category2Questions + category3Questions
        saveUserQuestions(userId, allQuestions.map { it.id })
        
        val creatorQueryData = CreatorQueryData(creatorUserId1, creatorId1, "카테고리마스터", "zzzz", 4.9, 300, 150)
        val userQueryData = UserQueryData(creatorUserId1, "이카테고리", "category_master.jpg", "category@test.com")
        
        given(creatorQueryAPI.getCreators(any()))
            .willReturn(listOf(creatorQueryData))
        given(userQueryAPI.getUsers(any()))
            .willReturn(listOf(userQueryData))
        
        val questionFilter = QuestionFilter(
            categories = listOf(category1, category2),
            sort = QuestionSortType.Latest
        )
        val pagingInformation = PagingInformation(0, 10)
        
        //when
        val libraryContents = libraryContentReader.getUserQuestions(userId, questionFilter, pagingInformation)
        
        //then
        Assertions.assertThat(libraryContents).hasSize(3)
        libraryContents.forEach { content ->
            Assertions.assertThat(content.creator.name).isEqualTo("이카테고리")
        }
    }
    
    @Test
    fun `문제 타입별로 필터링하여 조회할 수 있다`() {
        //given
        val selfMadeQuestions = listOf(
            QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionType = QuestionType.SelfMade,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            ),
            QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionType = QuestionType.SelfMade,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
        )
        val pastQuestions = listOf(
            QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionType = QuestionType.Past,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
        )
        
        val allQuestions = selfMadeQuestions + pastQuestions
        saveUserQuestions(userId, allQuestions.map { it.id })
        
        val creatorQueryData = CreatorQueryData(creatorUserId1, creatorId1, "자작문제전문가", "zzzz", 4.8, 150, 75)
        val userQueryData = UserQueryData(creatorUserId1, "최자작", "selfmade_expert.jpg", "selfmade@test.com")
        
        given(creatorQueryAPI.getCreators(any()))
            .willReturn(listOf(creatorQueryData))
        given(userQueryAPI.getUsers(any()))
            .willReturn(listOf(userQueryData))
        
        val questionFilter = QuestionFilter(
            questionType = QuestionType.SelfMade,
            sort = QuestionSortType.Latest
        )
        val pagingInformation = PagingInformation(0, 10)
        
        //when
        val libraryContents = libraryContentReader.getUserQuestions(userId, questionFilter, pagingInformation)
        
        //then
        Assertions.assertThat(libraryContents).hasSize(2)
        libraryContents.forEach { content ->
            Assertions.assertThat(content.creator.name).isEqualTo("최자작")
        }
    }
    
    private fun saveUserQuestions(userId: Long, questionIds: List<Long>) {
        val userQuestions = UserQuestion.create(userId, questionIds)
        userQuestionRepository.saveAll(userQuestions)
    }
}
