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
import com.eager.questioncloud.test.utils.DBCleaner
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class LibraryContentReaderTest(
    private val libraryContentReader: LibraryContentReader,
    private val userQuestionRepository: UserQuestionRepository,
    private val questionRepository: QuestionRepository,
    private val questionMetadataRepository: QuestionMetadataRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    
    @MockkBean
    private lateinit var creatorQueryAPI: CreatorQueryAPI
    
    @MockkBean
    private lateinit var userQueryAPI: UserQueryAPI
    
    private val userId = 1L
    private val creatorId1 = 101L
    private val creatorId2 = 102L
    private val creatorUserId1 = 201L
    private val creatorUserId2 = 202L
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("사용자가 여러 문제를 보유하고 있을 때") {
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
            
            every { creatorQueryAPI.getCreators(any()) } returns listOf(creatorQueryData1, creatorQueryData2)
            every { userQueryAPI.getUsers(any()) } returns listOf(userQueryData1, userQueryData2)
            
            val questionFilter = QuestionFilter(sort = QuestionSortType.Latest)
            val pagingInformation = PagingInformation(0, 10)
            
            When("사용자의 문제 목록을 조회하면") {
                val libraryContents = libraryContentReader.getUserQuestions(userId, questionFilter, pagingInformation)
                
                Then("모든 문제 정보가 반환된다") {
                    libraryContents shouldHaveSize 2
                    
                    val creatorIds = libraryContents.map { it.content.creatorId }
                    creatorIds shouldContainExactlyInAnyOrder listOf(creatorId1, creatorId2)
                    
                    val creator1Content = libraryContents.find { it.content.creatorId == creatorId1 }
                    creator1Content!!.creator.name shouldBe "수학선생님"
                    creator1Content.creator.mainSubject shouldBe "수학"
                    
                    val creator2Content = libraryContents.find { it.content.creatorId == creatorId2 }
                    creator2Content!!.creator.name shouldBe "영어선생님"
                    creator2Content.creator.mainSubject shouldBe "영어"
                }
            }
        }
        
        Given("사용자가 5개의 문제를 보유하고 있을 때") {
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
            
            When("사용자의 문제 개수를 조회하면") {
                val count = libraryContentReader.countUserQuestions(userId, questionFilter)
                
                Then("전체 문제 개수가 반환된다") {
                    count shouldBe 5
                }
            }
        }
        
        Given("특정 크리에이터의 문제가 여러 개 있을 때") {
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
            
            every { creatorQueryAPI.getCreators(any()) } returns listOf(creatorQueryData1)
            every { userQueryAPI.getUsers(any()) } returns listOf(userQueryData1)
            
            val questionFilter = QuestionFilter(creatorId = creatorId1, sort = QuestionSortType.Latest)
            val pagingInformation = PagingInformation(0, 10)
            
            When("특정 크리에이터로 필터링하여 조회하면") {
                val libraryContents = libraryContentReader.getUserQuestions(userId, questionFilter, pagingInformation)
                
                Then("해당 크리에이터의 문제만 반환된다") {
                    libraryContents shouldHaveSize 3
                    libraryContents.forEach { content ->
                        content.content.creatorId shouldBe creatorId1
                        content.creator.name shouldBe "김수학"
                    }
                }
            }
        }
        
        Given("다양한 난이도의 문제가 있을 때") {
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
            
            every { creatorQueryAPI.getCreators(any()) } returns listOf(creatorQueryData)
            every { userQueryAPI.getUsers(any()) } returns listOf(userQueryData)
            
            val questionFilter = QuestionFilter(
                levels = listOf(QuestionLevel.LEVEL1, QuestionLevel.LEVEL3),
                sort = QuestionSortType.Latest
            )
            val pagingInformation = PagingInformation(0, 10)
            
            When("특정 난이도로 필터링하여 조회하면") {
                val libraryContents = libraryContentReader.getUserQuestions(userId, questionFilter, pagingInformation)
                
                Then("해당 난이도의 문제만 반환된다") {
                    libraryContents shouldHaveSize 3
                    val levels = libraryContents.map { it.content.questionLevel }
                    levels shouldContainOnly listOf(QuestionLevel.LEVEL1, QuestionLevel.LEVEL3)
                    libraryContents.forEach { content ->
                        content.creator.name shouldBe "박난이도"
                    }
                }
            }
        }
        
        Given("다양한 카테고리의 문제가 있을 때") {
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
            
            every { creatorQueryAPI.getCreators(any()) } returns listOf(creatorQueryData)
            every { userQueryAPI.getUsers(any()) } returns listOf(userQueryData)
            
            val questionFilter = QuestionFilter(
                categories = listOf(category1, category2),
                sort = QuestionSortType.Latest
            )
            val pagingInformation = PagingInformation(0, 10)
            
            When("특정 카테고리로 필터링하여 조회하면") {
                val libraryContents = libraryContentReader.getUserQuestions(userId, questionFilter, pagingInformation)
                
                Then("해당 카테고리의 문제만 반환된다") {
                    libraryContents shouldHaveSize 3
                    libraryContents.forEach { content ->
                        content.creator.name shouldBe "이카테고리"
                    }
                }
            }
        }
        
        Given("다양한 타입의 문제가 있을 때") {
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
            
            every { creatorQueryAPI.getCreators(any()) } returns listOf(creatorQueryData)
            every { userQueryAPI.getUsers(any()) } returns listOf(userQueryData)
            
            val questionFilter = QuestionFilter(
                questionType = QuestionType.SelfMade,
                sort = QuestionSortType.Latest
            )
            val pagingInformation = PagingInformation(0, 10)
            
            When("특정 타입으로 필터링하여 조회하면") {
                val libraryContents = libraryContentReader.getUserQuestions(userId, questionFilter, pagingInformation)
                
                Then("해당 타입의 문제만 반환된다") {
                    libraryContents shouldHaveSize 2
                    libraryContents.forEach { content ->
                        content.creator.name shouldBe "최자작"
                    }
                }
            }
        }
    }
    
    private fun saveUserQuestions(userId: Long, questionIds: List<Long>) {
        val userQuestions = UserQuestion.create(userId, questionIds)
        userQuestionRepository.saveAll(userQuestions)
    }
}