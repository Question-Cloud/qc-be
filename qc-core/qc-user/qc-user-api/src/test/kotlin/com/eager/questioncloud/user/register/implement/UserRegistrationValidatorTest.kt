package com.eager.questioncloud.user.register.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.user.domain.UserAccountInformation
import com.eager.questioncloud.user.domain.UserInformation
import com.eager.questioncloud.user.enums.AccountType
import com.eager.questioncloud.user.repository.UserRepository
import com.eager.questioncloud.user.scenario.UserScenario
import com.eager.questioncloud.utils.DBCleaner
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
class UserRegistrationValidatorTest(
    private val userRegistrationValidator: UserRegistrationValidator,
    private val userRepository: UserRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("중복되지 않은 이메일 계정 정보가 주어졌을 때") {
            val userAccountInformation = UserAccountInformation.createEmailAccountInformation("password123")
            val userInformation = UserInformation(
                email = "new@example.com",
                phone = "010-1234-5678",
                name = "신규 사용자"
            )
            
            When("검증하면") {
                Then("예외가 발생하지 않는다") {
                    shouldNotThrowAny {
                        userRegistrationValidator.validate(userAccountInformation, userInformation)
                    }
                }
            }
        }
        
        Given("중복되지 않은 소셜 계정 정보가 주어졌을 때") {
            val userAccountInformation =
                UserAccountInformation.createSocialAccountInformation("new_kakao_uid", AccountType.KAKAO)
            val userInformation = UserInformation(
                email = "new@kakao.com",
                phone = "010-1234-5678",
                name = "신규 카카오 사용자"
            )
            
            When("검증하면") {
                Then("예외가 발생하지 않는다") {
                    shouldNotThrowAny {
                        userRegistrationValidator.validate(userAccountInformation, userInformation)
                    }
                }
            }
        }
        
        Given("중복된 이메일이 존재할 때") {
            val existingUser = userRepository.save(UserScenario.createEmailUser("password123"))
            
            val userAccountInformation = UserAccountInformation.createEmailAccountInformation("newpassword123")
            val userInformation = UserInformation(
                email = existingUser.userInformation.email,
                phone = "010-9999-8888",
                name = "중복 이메일 사용자"
            )
            
            When("중복된 이메일로 검증하면") {
                Then("중복 이메일 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        userRegistrationValidator.validate(userAccountInformation, userInformation)
                    }
                    exception.error shouldBe Error.DUPLICATE_EMAIL
                }
            }
        }
        
        Given("중복된 전화번호가 존재할 때") {
            val existingUser = userRepository.save(UserScenario.createEmailUser("password123"))
            val duplicatePhone = existingUser.userInformation.phone
            
            val userAccountInformation = UserAccountInformation.createEmailAccountInformation("password123")
            val userInformation = UserInformation(
                email = "new@example.com",
                phone = duplicatePhone,
                name = "중복 전화번호 사용자"
            )
            
            When("중복된 전화번호로 검증하면") {
                Then("중복 전화번호 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        userRegistrationValidator.validate(userAccountInformation, userInformation)
                    }
                    exception.error shouldBe Error.DUPLICATE_PHONE
                }
            }
        }
        
        Given("중복된 소셜 UID가 존재할 때") {
            val existingUser = userRepository.save(UserScenario.createSocialUser())
            val duplicateSocialUid = existingUser.userAccountInformation.socialUid!!
            
            val userAccountInformation =
                UserAccountInformation.createSocialAccountInformation(duplicateSocialUid, existingUser.userAccountInformation.accountType)
            val userInformation = UserInformation(
                email = "new@kakao.com",
                phone = "010-2222-2222",
                name = "중복 소셜 사용자"
            )
            
            When("중복된 소셜 UID로 검증하면") {
                Then("중복 소셜 UID 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        userRegistrationValidator.validate(userAccountInformation, userInformation)
                    }
                    exception.error shouldBe Error.DUPLICATE_SOCIAL_UID
                }
            }
        }
    }
}