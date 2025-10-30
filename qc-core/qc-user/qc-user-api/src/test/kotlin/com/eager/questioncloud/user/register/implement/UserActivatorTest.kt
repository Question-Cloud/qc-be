package com.eager.questioncloud.user.register.implement

import com.eager.questioncloud.point.api.internal.PointCommandAPI
import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.repository.UserRepository
import com.eager.questioncloud.user.scenario.UserScenario
import com.eager.questioncloud.user.scenario.custom
import com.eager.questioncloud.test.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.justRun
import io.mockk.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class UserActivatorTest(
    private val userActivator: UserActivator,
    private val userRepository: UserRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    lateinit var pointCommandAPI: PointCommandAPI
    
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("이메일 인증 대기 중인 사용자가 존재할 때") {
            val user = userRepository.save(
                UserScenario.createEmailUser("password123")
                    .custom { set(User::userStatus, UserStatus.PendingEmailVerification) }
            )
            
            user.userStatus shouldBe UserStatus.PendingEmailVerification
            
            justRun { pointCommandAPI.initialize(user.uid) }
            
            When("사용자를 활성화하면") {
                userActivator.activate(user.uid)
                
                Then("사용자 상태가 활성화되고 포인트가 초기화된다") {
                    val activatedUser = userRepository.getUser(user.uid)
                    activatedUser.userStatus shouldBe UserStatus.Active
                    verify(exactly = 1) { pointCommandAPI.initialize(user.uid) }
                }
            }
        }
    }
}