package com.eager.questioncloud.user.register.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.point.api.internal.PointCommandAPI
import com.eager.questioncloud.social.SocialAPIManager
import com.eager.questioncloud.user.dto.CreateUser
import com.eager.questioncloud.user.enums.AccountType
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.enums.UserType
import com.eager.questioncloud.user.repository.UserRepository
import com.eager.questioncloud.user.scenario.UserScenario
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class UserRegisterTest(
    private val userRegister: UserRegister,
    private val userRepository: UserRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    lateinit var socialAPIManager: SocialAPIManager
    
    @MockkBean
    lateinit var pointCommandAPI: PointCommandAPI
    
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("이메일 계정 사용자 생성 요청이 주어졌을 때") {
            val createUser = CreateUser(
                email = "test@example.com",
                password = "password123",
                socialRegisterToken = null,
                accountType = AccountType.EMAIL,
                phone = "010-1234-5678",
                name = "테스트 사용자"
            )
            
            justRun { pointCommandAPI.initialize(any()) }
            
            When("사용자를 생성하면") {
                val createdUser = userRegister.create(createUser)
                
                Then("이메일 계정 사용자가 생성된다") {
                    createdUser.uid shouldNotBe 0
                    createdUser.userInformation.email shouldBe "test@example.com"
                    createdUser.userInformation.name shouldBe "테스트 사용자"
                    createdUser.userInformation.phone shouldBe "010-1234-5678"
                    createdUser.userType shouldBe UserType.NormalUser
                    createdUser.userStatus shouldBe UserStatus.PendingEmailVerification
                    createdUser.userAccountInformation.accountType shouldBe AccountType.EMAIL
                    createdUser.userAccountInformation.isSocialAccount shouldBe false
                }
            }
        }
        
        Given("카카오 소셜 계정 사용자 생성 요청이 주어졌을 때") {
            every { socialAPIManager.getSocialUid(any(), any()) } returns "kakao_social_uid_12345"
            
            val createUser = CreateUser(
                email = "kakao@example.com",
                password = null,
                socialRegisterToken = "mock_kakao_token",
                accountType = AccountType.KAKAO,
                phone = "010-1234-5678",
                name = "카카오 사용자"
            )
            
            justRun { pointCommandAPI.initialize(any()) }
            
            When("사용자를 생성하면") {
                val createdUser = userRegister.create(createUser)
                
                Then("카카오 소셜 계정 사용자가 생성된다") {
                    createdUser.uid shouldNotBe 0
                    createdUser.userInformation.email shouldBe "kakao@example.com"
                    createdUser.userInformation.name shouldBe "카카오 사용자"
                    createdUser.userAccountInformation.accountType shouldBe AccountType.KAKAO
                    createdUser.userAccountInformation.isSocialAccount shouldBe true
                    createdUser.userAccountInformation.socialUid shouldBe "kakao_social_uid_12345"
                }
            }
        }
        
        Given("구글 소셜 계정 사용자 생성 요청이 주어졌을 때") {
            every { socialAPIManager.getSocialUid(any(), any()) } returns "google_social_uid_67890"
            
            val createUser = CreateUser(
                email = "google@example.com",
                password = null,
                socialRegisterToken = "mock_google_token",
                accountType = AccountType.GOOGLE,
                phone = "010-9876-5432",
                name = "구글 사용자"
            )
            
            justRun { pointCommandAPI.initialize(any()) }
            
            When("사용자를 생성하면") {
                val createdUser = userRegister.create(createUser)
                
                Then("구글 소셜 계정 사용자가 생성된다") {
                    createdUser.uid shouldNotBe 0
                    createdUser.userInformation.email shouldBe "google@example.com"
                    createdUser.userInformation.name shouldBe "구글 사용자"
                    createdUser.userAccountInformation.accountType shouldBe AccountType.GOOGLE
                    createdUser.userAccountInformation.isSocialAccount shouldBe true
                    createdUser.userAccountInformation.socialUid shouldBe "google_social_uid_67890"
                }
            }
        }
        
        Given("네이버 소셜 계정 사용자 생성 요청이 주어졌을 때") {
            every { socialAPIManager.getSocialUid(any(), any()) } returns "naver_social_uid_11111"
            
            val createUser = CreateUser(
                email = "naver@example.com",
                password = null,
                socialRegisterToken = "mock_naver_token",
                accountType = AccountType.NAVER,
                phone = "010-5555-6666",
                name = "네이버 사용자"
            )
            
            justRun { pointCommandAPI.initialize(any()) }
            
            When("사용자를 생성하면") {
                val createdUser = userRegister.create(createUser)
                
                Then("네이버 소셜 계정 사용자가 생성된다") {
                    createdUser.uid shouldNotBe 0
                    createdUser.userInformation.email shouldBe "naver@example.com"
                    createdUser.userInformation.name shouldBe "네이버 사용자"
                    createdUser.userAccountInformation.accountType shouldBe AccountType.NAVER
                    createdUser.userAccountInformation.isSocialAccount shouldBe true
                    createdUser.userAccountInformation.socialUid shouldBe "naver_social_uid_11111"
                }
            }
        }
        
        Given("이미 등록된 이메일이 존재할 때") {
            val user = userRepository.save(UserScenario.createEmailUser("password"))
            
            val createUser = CreateUser(
                email = user.userInformation.email,
                password = "password456",
                socialRegisterToken = null,
                accountType = AccountType.EMAIL,
                phone = "010-1111-2222",
                name = "중복 사용자"
            )
            
            When("중복된 이메일로 사용자를 생성하면") {
                Then("중복 이메일 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        userRegister.create(createUser)
                    }
                    exception.error shouldBe Error.DUPLICATE_EMAIL
                }
            }
        }
        
        Given("이미 등록된 전화번호가 존재할 때") {
            val user = userRepository.save(UserScenario.createEmailUser("password"))
            
            val createUser = CreateUser(
                email = "test2@example.com",
                password = "password456",
                socialRegisterToken = null,
                accountType = AccountType.EMAIL,
                phone = user.userInformation.phone,
                name = "중복 전화번호 사용자"
            )
            
            When("중복된 전화번호로 사용자를 생성하면") {
                Then("중복 전화번호 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        userRegister.create(createUser)
                    }
                    exception.error shouldBe Error.DUPLICATE_PHONE
                }
            }
        }
        
        Given("이미 등록된 소셜 UID가 존재할 때") {
            val user = userRepository.save(UserScenario.createSocialUser())
            val duplicateSocialUid = user.userAccountInformation.socialUid!!
            
            every { socialAPIManager.getSocialUid(any(), any()) } returns duplicateSocialUid
            
            val createUser = CreateUser(
                email = "new@kakao.com",
                password = null,
                socialRegisterToken = "token2",
                accountType = AccountType.KAKAO,
                phone = "010-2222-2222",
                name = "중복 소셜 사용자"
            )
            
            When("중복된 소셜 UID로 사용자를 생성하면") {
                Then("중복 소셜 UID 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        userRegister.create(createUser)
                    }
                    exception.error shouldBe Error.DUPLICATE_SOCIAL_UID
                }
            }
        }
        
        Given("사용자 생성 요청이 주어졌을 때") {
            val createUser = CreateUser(
                email = "point@example.com",
                password = "password123",
                socialRegisterToken = null,
                accountType = AccountType.EMAIL,
                phone = "010-5555-6666",
                name = "포인트 테스트 사용자"
            )
            
            justRun { pointCommandAPI.initialize(any()) }
            
            When("사용자를 생성하면") {
                val createdUser = userRegister.create(createUser)
                
                Then("포인트가 초기화된다") {
                    createdUser.uid shouldNotBe 0
                    verify(exactly = 1) { pointCommandAPI.initialize(any()) }
                }
            }
        }
    }
}
