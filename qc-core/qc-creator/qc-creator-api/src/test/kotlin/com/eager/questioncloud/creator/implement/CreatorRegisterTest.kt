package com.eager.questioncloud.creator.implement

import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.user.api.internal.UserCommandAPI
import com.eager.questioncloud.test.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.ints.exactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.justRun
import org.mockito.kotlin.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class CreatorRegisterTest(
    private val creatorRegister: CreatorRegister,
    private val creatorRepository: CreatorRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var userCommandAPI: UserCommandAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("워크스페이스 크리에이터 등록") {
            val userId = 1L
            
            justRun { userCommandAPI.toCreator(userId) }
            
            When("크리에이터 정보를 입력하여 등록하면") {
                val mainSubject = "수학"
                val introduction = "안녕하세요. 수학 크리에이터입니다."
                
                val newCreator = creatorRegister.register(userId, mainSubject, introduction)
                
                Then("크리에이터로 등록된다.") {
                    val foundCreator = creatorRepository.findById(newCreator.id)
                    
                    foundCreator shouldNotBe null
                    foundCreator.userId shouldBe userId
                    foundCreator.mainSubject shouldBe mainSubject
                    foundCreator.introduction shouldBe introduction
                    
                    verify(exactly(1)) { userCommandAPI.toCreator(userId) }
                }
            }
        }
    }
}