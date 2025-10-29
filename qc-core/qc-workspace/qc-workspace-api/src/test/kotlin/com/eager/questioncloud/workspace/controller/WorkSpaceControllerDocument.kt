package com.eager.questioncloud.workspace.controller

import com.eager.questioncloud.application.security.JwtAuthenticationFilter
import com.eager.questioncloud.filter.FilterExceptionHandlerFilter
import com.eager.questioncloud.workspace.config.CreatorPrincipalResolverConfig
import com.eager.questioncloud.workspace.config.TestConfig
import com.eager.questioncloud.workspace.dto.CreatorPostItem
import com.eager.questioncloud.workspace.dto.CreatorProfile
import com.eager.questioncloud.workspace.dto.CreatorQuestionInformation
import com.eager.questioncloud.workspace.dto.MyQuestionContent
import com.eager.questioncloud.workspace.resolver.CreatorPrincipalArgumentResolver
import com.eager.questioncloud.workspace.service.WorkspacePostService
import com.eager.questioncloud.workspace.service.WorkspaceProfileService
import com.eager.questioncloud.workspace.service.WorkspaceQuestionService
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.justRun
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(
    controllers = [WorkSpaceController::class],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [JwtAuthenticationFilter::class]
        ),
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [FilterExceptionHandlerFilter::class]
        ),
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [CreatorPrincipalArgumentResolver::class]
        ),
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [CreatorPrincipalResolverConfig::class]
        ),
    ]
)
@Import(TestConfig::class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@ApplyExtension(SpringExtension::class)
class WorkSpaceControllerDocument(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) : FunSpec() {
    @MockkBean
    private lateinit var workspaceProfileService: WorkspaceProfileService
    
    @MockkBean
    private lateinit var workspacePostService: WorkspacePostService
    
    @MockkBean
    private lateinit var workspaceQuestionService: WorkspaceQuestionService
    
    private val sampleProfile = CreatorProfile(
        mainSubject = "Mathematics",
        introduction = "수학 전문 크리에이터입니다."
    )
    
    private val sampleQuestions = listOf(
        CreatorQuestionInformation(
            id = 1L,
            creatorId = 1L,
            title = "수능 수학 문제 1",
            subject = "Mathematics",
            parentCategory = "수학",
            childCategory = "미적분",
            thumbnail = "https://example.com/thumbnail1.jpg",
            questionLevel = "HARD",
            price = 5000,
            rate = 4.5
        ),
        CreatorQuestionInformation(
            id = 2L,
            creatorId = 1L,
            title = "수능 수학 문제 2",
            subject = "Mathematics",
            parentCategory = "수학",
            childCategory = "확률과 통계",
            thumbnail = "https://example.com/thumbnail2.jpg",
            questionLevel = "MEDIUM",
            price = 3000,
            rate = 4.8
        )
    )
    
    private val sampleQuestionContent = MyQuestionContent(
        questionCategoryId = 1L,
        subject = "Mathematics",
        title = "수능 수학 문제 1",
        description = "미적분 문제입니다.",
        thumbnail = "https://example.com/thumbnail.jpg",
        fileUrl = "https://example.com/question.pdf",
        explanationUrl = "https://example.com/explanation.pdf",
        questionLevel = "HARD",
        price = 5000
    )
    
    private val samplePosts = listOf(
        CreatorPostItem(
            id = 1L,
            title = "문제 관련 공지사항",
            writer = "김수학",
            createdAt = LocalDateTime.of(2025, 1, 15, 10, 30)
        ),
        CreatorPostItem(
            id = 2L,
            title = "새로운 문제 업데이트",
            writer = "김수학",
            createdAt = LocalDateTime.of(2025, 1, 20, 14, 20)
        )
    )
    
    init {
        test("크리에이터 프로필 조회 API 테스트") {
            every { workspaceProfileService.getProfile(any()) } returns sampleProfile
            
            mockMvc.perform(
                get("/api/workspace/me")
                    .header("Authorization", "Bearer mock_access_token")
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "워크스페이스 - 크리에이터 프로필 조회",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("크리에이터 프로필 조회")
                            .description("로그인한 크리에이터의 프로필 정보를 조회합니다.")
                            .tag("workspace"),
                        snippets = arrayOf(
                            responseFields(
                                fieldWithPath("profile").description("크리에이터 프로필"),
                                fieldWithPath("profile.mainSubject").description("주요 과목"),
                                fieldWithPath("profile.introduction").description("자기소개")
                            )
                        )
                    )
                )
        }
        
        test("크리에이터 프로필 수정 API 테스트") {
            justRun { workspaceProfileService.updateCreatorProfile(any(), any(), any()) }
            
            val requestBody = mapOf(
                "mainSubject" to "Physics",
                "introduction" to "물리학 전문 크리에이터입니다."
            )
            
            mockMvc.perform(
                patch("/api/workspace/me")
                    .header("Authorization", "Bearer mock_access_token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "워크스페이스 - 크리에이터 프로필 수정",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("크리에이터 프로필 수정")
                            .description("로그인한 크리에이터의 프로필 정보를 수정합니다.")
                            .tag("workspace"),
                        snippets = arrayOf(
                            requestFields(
                                fieldWithPath("mainSubject").description("주요 과목"),
                                fieldWithPath("introduction").description("자기소개")
                            ),
                            responseFields(
                                fieldWithPath("success").description("성공 여부")
                            )
                        )
                    )
                )
        }
        
        test("문제 목록 조회 API 테스트") {
            every { workspaceQuestionService.countMyQuestions(any()) } returns 2
            every { workspaceQuestionService.getMyQuestions(any(), any()) } returns sampleQuestions
            
            mockMvc.perform(
                get("/api/workspace/question")
                    .header("Authorization", "Bearer mock_access_token")
                    .param("page", "1")
                    .param("size", "10")
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "워크스페이스 - 문제 목록 조회",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("크리에이터 문제 목록 조회")
                            .description("로그인한 크리에이터가 등록한 문제 목록을 페이징하여 조회합니다.")
                            .tag("workspace"),
                        snippets = arrayOf(
                            queryParameters(
                                parameterWithName("page").description("페이지 번호 (1부터 시작)"),
                                parameterWithName("size").description("페이지 크기")
                            ),
                            responseFields(
                                fieldWithPath("total").description("전체 문제 수"),
                                fieldWithPath("result").description("문제 목록"),
                                fieldWithPath("result[].id").description("문제 ID"),
                                fieldWithPath("result[].creatorId").description("크리에이터 ID"),
                                fieldWithPath("result[].title").description("문제 제목"),
                                fieldWithPath("result[].subject").description("과목"),
                                fieldWithPath("result[].parentCategory").description("상위 카테고리"),
                                fieldWithPath("result[].childCategory").description("하위 카테고리"),
                                fieldWithPath("result[].thumbnail").description("썸네일 이미지 URL"),
                                fieldWithPath("result[].questionLevel").description("문제 난이도"),
                                fieldWithPath("result[].price").description("가격"),
                                fieldWithPath("result[].rate").description("평점")
                            )
                        )
                    )
                )
        }
        
        test("문제 상세 조회 API 테스트") {
            val questionId = 1L
            every { workspaceQuestionService.getMyQuestionContent(any(), any()) } returns sampleQuestionContent
            
            mockMvc.perform(
                get("/api/workspace/question/{questionId}", questionId)
                    .header("Authorization", "Bearer mock_access_token")
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "워크스페이스 - 문제 상세 조회",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("크리에이터 문제 상세 조회")
                            .description("로그인한 크리에이터가 등록한 특정 문제의 상세 정보를 조회합니다.")
                            .tag("workspace"),
                        snippets = arrayOf(
                            pathParameters(
                                parameterWithName("questionId").description("조회할 문제 ID")
                            ),
                            responseFields(
                                fieldWithPath("question").description("문제 정보"),
                                fieldWithPath("question.questionCategoryId").description("문제 카테고리 ID"),
                                fieldWithPath("question.subject").description("과목"),
                                fieldWithPath("question.title").description("문제 제목"),
                                fieldWithPath("question.description").description("문제 설명"),
                                fieldWithPath("question.thumbnail").description("썸네일 이미지 URL"),
                                fieldWithPath("question.fileUrl").description("문제 파일 URL"),
                                fieldWithPath("question.explanationUrl").description("해설 파일 URL"),
                                fieldWithPath("question.questionLevel").description("문제 난이도"),
                                fieldWithPath("question.price").description("가격")
                            )
                        )
                    )
                )
        }
        
        test("문제 등록 API 테스트") {
            justRun { workspaceQuestionService.registerQuestion(any()) }
            
            val requestBody = mapOf(
                "questionCategoryId" to 1L,
                "subject" to "Mathematics",
                "title" to "수능 수학 문제",
                "description" to "미적분 문제입니다.",
                "thumbnail" to "https://example.com/thumbnail.jpg",
                "fileUrl" to "https://example.com/question.pdf",
                "explanationUrl" to "https://example.com/explanation.pdf",
                "questionLevel" to "HARD",
                "price" to 5000
            )
            
            mockMvc.perform(
                post("/api/workspace/question")
                    .header("Authorization", "Bearer mock_access_token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "워크스페이스 - 문제 등록",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("문제 등록")
                            .description("로그인한 크리에이터가 새로운 문제를 등록합니다.")
                            .tag("workspace"),
                        snippets = arrayOf(
                            requestFields(
                                fieldWithPath("questionCategoryId").description("문제 카테고리 ID"),
                                fieldWithPath("subject").description("과목"),
                                fieldWithPath("title").description("문제 제목"),
                                fieldWithPath("description").description("문제 설명"),
                                fieldWithPath("thumbnail").description("썸네일 이미지 URL"),
                                fieldWithPath("fileUrl").description("문제 파일 URL"),
                                fieldWithPath("explanationUrl").description("해설 파일 URL"),
                                fieldWithPath("questionLevel").description("문제 난이도"),
                                fieldWithPath("price").description("가격 (최소 100원)")
                            ),
                            responseFields(
                                fieldWithPath("success").description("성공 여부")
                            )
                        )
                    )
                )
        }
        
        test("문제 수정 API 테스트") {
            val questionId = 1L
            justRun { workspaceQuestionService.modifyQuestion(any()) }
            
            val requestBody = mapOf(
                "questionCategoryId" to 1L,
                "subject" to "Mathematics",
                "title" to "수정된 수능 수학 문제",
                "description" to "수정된 미적분 문제입니다.",
                "thumbnail" to "https://example.com/updated-thumbnail.jpg",
                "fileUrl" to "https://example.com/updated-question.pdf",
                "explanationUrl" to "https://example.com/updated-explanation.pdf",
                "questionLevel" to "MEDIUM",
                "price" to 4000
            )
            
            mockMvc.perform(
                patch("/api/workspace/question/{questionId}", questionId)
                    .header("Authorization", "Bearer mock_access_token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "워크스페이스 - 문제 수정",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("문제 수정")
                            .description("로그인한 크리에이터가 등록한 문제를 수정합니다.")
                            .tag("workspace"),
                        snippets = arrayOf(
                            pathParameters(
                                parameterWithName("questionId").description("수정할 문제 ID")
                            ),
                            requestFields(
                                fieldWithPath("questionCategoryId").description("문제 카테고리 ID"),
                                fieldWithPath("subject").description("과목"),
                                fieldWithPath("title").description("문제 제목"),
                                fieldWithPath("description").description("문제 설명"),
                                fieldWithPath("thumbnail").description("썸네일 이미지 URL"),
                                fieldWithPath("fileUrl").description("문제 파일 URL"),
                                fieldWithPath("explanationUrl").description("해설 파일 URL"),
                                fieldWithPath("questionLevel").description("문제 난이도"),
                                fieldWithPath("price").description("가격 (최소 100원)")
                            ),
                            responseFields(
                                fieldWithPath("success").description("성공 여부")
                            )
                        )
                    )
                )
        }
        
        test("문제 삭제 API 테스트") {
            val questionId = 1L
            justRun { workspaceQuestionService.deleteQuestion(any()) }
            
            mockMvc.perform(
                delete("/api/workspace/question/{questionId}", questionId)
                    .header("Authorization", "Bearer mock_access_token")
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "워크스페이스 - 문제 삭제",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("문제 삭제")
                            .description("로그인한 크리에이터가 등록한 문제를 삭제합니다.")
                            .tag("workspace"),
                        snippets = arrayOf(
                            pathParameters(
                                parameterWithName("questionId").description("삭제할 문제 ID")
                            ),
                            responseFields(
                                fieldWithPath("success").description("성공 여부")
                            )
                        )
                    )
                )
        }
        
        test("크리에이터 게시글 목록 조회 API 테스트") {
            every { workspacePostService.countCreatorPost(any()) } returns 2
            every { workspacePostService.getCreatorPosts(any(), any()) } returns samplePosts
            
            mockMvc.perform(
                get("/api/workspace/board")
                    .header("Authorization", "Bearer mock_access_token")
                    .param("page", "1")
                    .param("size", "10")
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "워크스페이스 - 크리에이터 게시글 목록 조회",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("크리에이터 게시글 목록 조회")
                            .description("로그인한 크리에이터가 작성한 게시글 목록을 페이징하여 조회합니다.")
                            .tag("workspace"),
                        snippets = arrayOf(
                            queryParameters(
                                parameterWithName("page").description("페이지 번호 (1부터 시작)"),
                                parameterWithName("size").description("페이지 크기")
                            ),
                            responseFields(
                                fieldWithPath("total").description("전체 게시글 수"),
                                fieldWithPath("result").description("게시글 목록"),
                                fieldWithPath("result[].id").description("게시글 ID"),
                                fieldWithPath("result[].title").description("게시글 제목"),
                                fieldWithPath("result[].writer").description("작성자"),
                                fieldWithPath("result[].createdAt").description("작성일시")
                            )
                        )
                    )
                )
        }
    }
}