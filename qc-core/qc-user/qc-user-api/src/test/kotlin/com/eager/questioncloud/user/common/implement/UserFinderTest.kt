package com.eager.questioncloud.user.common.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.user.repository.UserRepository
import com.eager.questioncloud.user.scenario.UserScenario
import com.eager.questioncloud.utils.DBCleaner
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
class UserFinderTest(
    private val userFinder: UserFinder,
    private val userRepository: UserRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("존재하는 사용자를 ID로 조회할 때") {
            val user = userRepository.save(UserScenario.createEmailUser("password"))
            
            When("사용자를 조회하면") {
                val foundUser = userFinder.getById(user.uid)
                
                Then("해당 사용자가 반환된다") {
                    foundUser.uid shouldBe user.uid
                }
            }
        }
        
        Given("존재하지 않는 사용자를 ID로 조회할 때") {
            When("사용자를 조회하면") {
                Then("CoreException(Error.NOT_FOUND)이 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        userFinder.getById(999999L)
                    }
                    exception.error shouldBe Error.NOT_FOUND
                }
            }
        }
        
        Given("존재하는 사용자를 이메일로 조회할 때") {
            val user = userRepository.save(UserScenario.createEmailUser("password"))
            
            When("사용자를 조회하면") {
                val foundUser = userFinder.getByEmail(user.userInformation.email)
                
                Then("해당 사용자가 반환된다") {
                    foundUser.uid shouldBe user.uid
                    foundUser.userInformation.email shouldBe user.userInformation.email
                }
            }
        }
        
        Given("존재하지 않는 사용자를 이메일로 조회할 때") {
            When("사용자를 조회하면") {
                Then("CoreException(Error.NOT_FOUND)이 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        userFinder.getByEmail("nonexistent@example.com")
                    }
                    exception.error shouldBe Error.NOT_FOUND
                }
            }
        }
        
        Given("존재하는 사용자를 전화번호로 조회할 때") {
            val user = userRepository.save(UserScenario.createEmailUser("password"))
            
            When("사용자를 조회하면") {
                val foundUser = userFinder.getByPhone(user.userInformation.phone)
                
                Then("해당 사용자가 반환된다") {
                    foundUser.uid shouldBe user.uid
                    foundUser.userInformation.phone shouldBe user.userInformation.phone
                }
            }
        }
        
        Given("존재하지 않는 사용자를 전화번호로 조회할 때") {
            When("사용자를 조회하면") {
                Then("CoreException(Error.NOT_FOUND)이 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        userFinder.getByPhone("010-9999-9999")
                    }
                    exception.error shouldBe Error.NOT_FOUND
                }
            }
        }
    }
}
