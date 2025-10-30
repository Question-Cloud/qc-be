package com.eager.questioncloud.user.account.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.user.repository.UserRepository
import com.eager.questioncloud.user.scenario.UserScenario
import com.eager.questioncloud.test.utils.DBCleaner
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class UserAccountUpdaterTest(
    private val userAccountUpdater: UserAccountUpdater,
    private val userRepository: UserRepository,
    private val dbCleaner: DBCleaner
) : BehaviorSpec() {
    
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("이메일 계정 사용자가 있을 때") {
            val password = "password"
            val user = userRepository.save(UserScenario.createEmailUser(password))
            val newPassword = "newPassword456"
            
            When("비밀번호를 변경하면") {
                userAccountUpdater.changePassword(user.uid, newPassword)
                
                Then("비밀번호 변경이 처리된다.") {
                    val updatedUser = userRepository.getUser(user.uid)
                    
                    shouldNotThrowAny {
                        updatedUser.passwordAuthentication(newPassword)
                    }
                    
                    shouldThrow<CoreException> {
                        updatedUser.passwordAuthentication(user.userAccountInformation.password!!)
                    }.error shouldBe Error.FAIL_LOGIN
                }
            }
        }
        
        Given("소셜 계정 사용자가 있을 때") {
            val socialUser = userRepository.save(UserScenario.createSocialUser())
            val newPassword = "attemptedPassword123"
            
            When("비밀번호를 변경하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        userAccountUpdater.changePassword(socialUser.uid, newPassword)
                    }.error shouldBe Error.NOT_PASSWORD_SUPPORT_ACCOUNT
                }
            }
        }
    }
}