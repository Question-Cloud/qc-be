package com.eager.questioncloud.question.library.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.dto.UserQuestionContent
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionSortType
import com.eager.questioncloud.question.library.dto.ContentCreator
import com.eager.questioncloud.question.library.dto.LibraryContent
import com.eager.questioncloud.question.library.implement.LibraryContentReader
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk

class LibraryServiceTest : BehaviorSpec() {
    private val libraryContentReader = mockk<LibraryContentReader>()
    private val libraryService = LibraryService(libraryContentReader)
    
    private val userId = 1L
    private val creatorId = 101L
    
    init {
        afterEach {
            clearMocks(libraryContentReader)
        }
        
        Given("사용자가 보유한 문제가 존재할 때") {
            val questionFilter = QuestionFilter(sort = QuestionSortType.Latest)
            val pagingInformation = PagingInformation(0, 10)
            
            val userQuestionContent = UserQuestionContent(
                questionId = 1L,
                creatorId = creatorId,
                title = "수학 문제 1",
                parentCategory = "수학",
                childCategory = "미적분",
                thumbnail = "thumbnail.jpg",
                questionLevel = QuestionLevel.LEVEL3,
                fileUrl = "question.pdf",
                explanationUrl = "explanation.pdf"
            )
            
            val contentCreator = ContentCreator(
                name = "수학선생님",
                profileImage = "profile.jpg",
                mainSubject = "수학"
            )
            
            val libraryContent = LibraryContent(userQuestionContent, contentCreator)
            val expectedResult = listOf(libraryContent)
            
            every { libraryContentReader.getUserQuestions(userId, questionFilter, pagingInformation) } returns expectedResult
            
            When("사용자의 문제 목록을 조회하면") {
                val result = libraryService.getUserQuestions(userId, questionFilter, pagingInformation)
                
                Then("사용자의 문제 목록이 반환된다") {
                    result shouldHaveSize 1
                    result[0].content.title shouldBe "수학 문제 1"
                    result[0].creator.name shouldBe "수학선생님"
                    result[0].creator.mainSubject shouldBe "수학"
                }
            }
        }
        
        Given("사용자가 여러 문제를 보유하고 있을 때") {
            val questionFilter = QuestionFilter(sort = QuestionSortType.Latest)
            val expectedCount = 5
            
            every { libraryContentReader.countUserQuestions(userId, questionFilter) } returns expectedCount
            
            When("사용자의 문제 개수를 조회하면") {
                val result = libraryService.countUserQuestions(userId, questionFilter)
                
                Then("보유한 문제 개수가 반환된다") {
                    result shouldBe 5
                }
            }
        }
    }
}
