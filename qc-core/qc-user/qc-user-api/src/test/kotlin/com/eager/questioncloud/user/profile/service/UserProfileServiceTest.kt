package com.eager.questioncloud.user.profile.service

import com.eager.questioncloud.user.repository.UserRepository
import com.eager.questioncloud.user.scenario.UserScenario
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserProfileServiceTest : BehaviorSpec() {
    private val userRepository = mockk<UserRepository>()
    private val userProfileService = UserProfileService(userRepository)
    
    init {
        Given("사용자 정보 업데이트 - 사용자가 존재할 때") {
            val password = "password123"
            val user = UserScenario.createEmailUser(password)
            val newName = "업데이트된 이름"
            val newProfileImage = "https://example.com/new-profile.jpg"
            
            every { userRepository.getUser(user.uid) } returns user
            every { userRepository.save(user) } returns user
            
            When("사용자 정보를 업데이트하면") {
                userProfileService.updateUserInformation(user.uid, newName, newProfileImage)
                
                Then("사용자 정보가 변경된다") {
                    verify(exactly = 1) { userRepository.getUser(user.uid) }
                    verify(exactly = 1) { userRepository.save(user) }
                    
                    user.userInformation.name shouldBe newName
                    user.userInformation.profileImage shouldBe newProfileImage
                }
            }
        }
        
        Given("내 정보 조회 - 사용자가 존재할 때") {
            val password = "password123"
            val user = UserScenario.createEmailUser(password)
            
            every { userRepository.getUser(user.uid) } returns user
            
            When("내 정보를 조회하면") {
                val myInformation = userProfileService.getMyInformation(user.uid)
                
                Then("사용자 정보를 반환한다") {
                    verify(exactly = 1) { userRepository.getUser(user.uid) }
                    
                    myInformation.email shouldBe user.userInformation.email
                    myInformation.name shouldBe user.userInformation.name
                    myInformation.profileImage shouldBe user.userInformation.profileImage
                }
            }
        }
    }
}
