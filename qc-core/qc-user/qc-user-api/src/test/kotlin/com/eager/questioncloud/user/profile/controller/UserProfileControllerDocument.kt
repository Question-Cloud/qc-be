package com.eager.questioncloud.user.profile.controller

import com.eager.questioncloud.user.dto.MyInformation
import com.eager.questioncloud.user.enums.UserType
import com.eager.questioncloud.user.profile.dto.UpdateMyInformationRequest
import com.eager.questioncloud.user.profile.service.UserProfileService
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class UserProfileControllerDocument {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var userProfileService: UserProfileService

    private lateinit var sampleMyInformation: MyInformation

    @BeforeEach
    fun setUp() {
        sampleMyInformation = MyInformation(
            profileImage = "https://example.com/profile/user123.jpg",
            name = "김사용자",
            email = "user@example.com",
            phone = "01012345678",
            userType = UserType.NormalUser
        )
    }

    @Test
    fun `내 정보 조회 API 테스트`() {
        // When & Then
        whenever(userProfileService.getMyInformation(any())).thenReturn(sampleMyInformation)
        mockMvc.perform(
            get("/api/user/profile/me")
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "내 정보 조회",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("내 정보 조회")
                        .description("현재 로그인한 사용자의 정보를 조회합니다.")
                        .tag("user-profile"),
                    snippets = arrayOf(
                        responseFields(
                            fieldWithPath("myInformation").description("내 정보"),
                            fieldWithPath("myInformation.profileImage").description("프로필 이미지 URL"),
                            fieldWithPath("myInformation.name").description("사용자 이름"),
                            fieldWithPath("myInformation.email").description("이메일"),
                            fieldWithPath("myInformation.phone").description("전화번호"),
                            fieldWithPath("myInformation.userType").description("사용자 타입 (GeneralUser: 일반사용자, CreatorUser: 크리에이터)")
                        )
                    )
                )
            )
    }

    @Test
    fun `내 정보 수정 API 테스트`() {
        // Given
        val updateRequest = UpdateMyInformationRequest(
            profileImage = "https://example.com/profile/updated_user123.jpg",
            name = "김수정자"
        )

        // When & Then
        mockMvc.perform(
            patch("/api/user/profile/me")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "내 정보 수정",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("내 정보 수정")
                        .description("현재 로그인한 사용자의 정보를 수정합니다.")
                        .tag("user-profile"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("profileImage").description("수정할 프로필 이미지 URL (선택사항)").optional(),
                            fieldWithPath("name").description("수정할 사용자 이름")
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }
}
