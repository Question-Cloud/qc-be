package com.eager.questioncloud.user.profile.service

import com.eager.questioncloud.user.common.implement.UserFinder
import com.eager.questioncloud.user.profile.implement.UserProfileUpdater
import com.eager.questioncloud.user.scenario.UserScenario
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

class UserProfileServiceTest : BehaviorSpec() {
    private val userFinder = mockk<UserFinder>()
    private val userProfileUpdater = mockk<UserProfileUpdater>()
    private val userProfileService = UserProfileService(userFinder, userProfileUpdater)
    
    init {
        Given("내 정보 업데이트") {
            val password = "password123"
            val user = UserScenario.createEmailUser(password)
            val newName = "업데이트된 이름"
            val newProfileImage = "https://example.com/new-profile.jpg"
            
            justRun { userProfileUpdater.updateUserInformation(user.uid, newName, newProfileImage) }
            
            When("사용자 정보를 업데이트하면") {
                userProfileService.updateUserInformation(user.uid, newName, newProfileImage)
                
                Then("사용자 정보가 변경된다") {
                    verify(exactly = 1) { userProfileUpdater.updateUserInformation(user.uid, newName, newProfileImage) }
                }
            }
        }
        
        Given("내 정보 조회") {
            val password = "password123"
            val user = UserScenario.createEmailUser(password)
            
            every { userFinder.getById(user.uid) } returns user
            
            When("내 정보를 조회하면") {
                val myInformation = userProfileService.getMyInformation(user.uid)
                
                Then("사용자 정보를 반환한다") {
                    myInformation.email shouldBe user.userInformation.email
                    myInformation.name shouldBe user.userInformation.name
                    myInformation.profileImage shouldBe user.userInformation.profileImage
                }
            }
        }
    }
}
