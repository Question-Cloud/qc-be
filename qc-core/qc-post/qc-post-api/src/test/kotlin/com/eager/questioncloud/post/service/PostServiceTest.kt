package com.eager.questioncloud.post.service

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.command.*
import com.eager.questioncloud.post.domain.Post
import com.eager.questioncloud.post.domain.PostContent
import com.eager.questioncloud.post.domain.PostFile
import com.eager.questioncloud.post.dto.PostDetail
import com.eager.questioncloud.post.dto.PostPreview
import com.eager.questioncloud.post.implement.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

class PostServiceTest : BehaviorSpec() {
    private val postPermissionValidator = mockk<PostPermissionValidator>()
    private val postRegister = mockk<PostRegister>()
    private val postReader = mockk<PostReader>()
    private val postUpdater = mockk<PostUpdater>()
    private val postRemover = mockk<PostRemover>()
    
    private val postService = PostService(
        postPermissionValidator,
        postRegister,
        postReader,
        postUpdater,
        postRemover
    )
    
    init {
        Given("게시글 등록") {
            val userId = 1L
            val questionId = 1L
            
            val registerPostCommand = RegisterPostCommand(
                questionId = questionId,
                writerId = userId,
                postContent = RegisterPostCommandPostContent(
                    title = "테스트 게시글",
                    content = "게시글 내용입니다",
                    files = emptyList()
                )
            )
            
            val savedPost = Post.create(
                questionId,
                userId,
                PostContent.create("테스트 게시글", "게시글 내용입니다", emptyList())
            )
            
            every { postRegister.register(registerPostCommand) } returns savedPost
            justRun { postPermissionValidator.validatePostPermission(any(), any()) }
            
            When("게시글을 등록하면") {
                val result = postService.register(registerPostCommand)
                
                Then("게시글이 등록된다") {
                    result shouldNotBe null
                    result.questionId shouldBe questionId
                    result.writerId shouldBe userId
                    result.postContent.title shouldBe "테스트 게시글"
                    result.postContent.content shouldBe "게시글 내용입니다"
                    
                    verify(exactly = 1) { postRegister.register(registerPostCommand) }
                }
            }
        }
        
        Given("게시글 목록 조회") {
            val userId = 1L
            val questionId = 1L
            val pagingInformation = PagingInformation(0, 10)
            
            val postPreviews = listOf(
                PostPreview(
                    id = 1L,
                    title = "첫 번째 게시글",
                    writer = "작성자1",
                    createdAt = LocalDateTime.now()
                ),
                PostPreview(
                    id = 2L,
                    title = "두 번째 게시글",
                    writer = "작성자2",
                    createdAt = LocalDateTime.now()
                )
            )
            
            every { postReader.getPostPreviews(userId, questionId, pagingInformation) } returns postPreviews
            justRun { postPermissionValidator.validatePostPermission(any(), any()) }
            
            When("게시글 목록을 조회하면") {
                val result = postService.getPostPreviews(userId, questionId, pagingInformation)
                
                Then("게시글 목록이 반환된다") {
                    result.size shouldBe 2
                    
                    val preview1 = result.find { it.title == "첫 번째 게시글" }
                    preview1 shouldNotBe null
                    preview1!!.writer shouldBe "작성자1"
                    
                    val preview2 = result.find { it.title == "두 번째 게시글" }
                    preview2 shouldNotBe null
                    preview2!!.writer shouldBe "작성자2"
                    
                    verify(exactly = 1) { postReader.getPostPreviews(userId, questionId, pagingInformation) }
                }
            }
        }
        
        Given("게시글 개수 조회") {
            val questionId = 1L
            
            every { postReader.countPost(questionId) } returns 3
            
            When("게시글 개수를 조회하면") {
                val result = postService.countPost(questionId)
                
                Then("게시글 개수가 반환된다") {
                    result shouldBe 3
                    
                    verify(exactly = 1) { postReader.countPost(questionId) }
                }
            }
        }
        
        Given("게시글 상세 정보 조회") {
            val userId = 1L
            val postId = 1L
            val questionId = 1L
            
            val postDetail = PostDetail(
                id = postId,
                questionId = questionId,
                title = "상세 게시글",
                content = "상세 내용입니다",
                files = listOf(
                    PostFile("file1.txt", "https://example.com/file1.txt"),
                    PostFile("file2.jpg", "https://example.com/file2.jpg")
                ),
                parentCategory = "수학",
                childCategory = "대수",
                questionTitle = "테스트 문제",
                writer = "게시글작성자",
                createdAt = LocalDateTime.now()
            )
            
            every { postReader.getPostDetail(userId, postId) } returns postDetail
            justRun { postPermissionValidator.validatePostPermission(any(), any()) }
            
            When("게시글 상세 정보를 조회하면") {
                val result = postService.getPostDetail(userId, postId)
                
                Then("게시글 상세 정보가 반환된다") {
                    result shouldNotBe null
                    result.title shouldBe "상세 게시글"
                    result.content shouldBe "상세 내용입니다"
                    result.files.size shouldBe 2
                    result.writer shouldBe "게시글작성자"
                    
                    verify(exactly = 1) { postReader.getPostDetail(userId, postId) }
                }
            }
        }
        
        Given("게시글 수정") {
            val userId = 1L
            val postId = 1L
            
            val modifyPostCommand = ModifyPostCommand(
                postId = postId,
                userId = userId,
                ModifyPostCommandPostContent(
                    title = "제목 수정",
                    content = "내용 수정",
                    files = emptyList()
                )
            )
            
            justRun { postService.modify(modifyPostCommand) }
            
            When("게시글을 수정하면") {
                postService.modify(modifyPostCommand)
                
                Then("게시글이 수정된다") {
                    verify(exactly = 1) { postUpdater.modify(any()) }
                }
            }
        }
        
        Given("게시글 삭제") {
            val userId = 1L
            val postId = 1L
            
            val deletePostCommand = DeletePostCommand(postId, userId)
            
            justRun { postRemover.delete(deletePostCommand) }
            
            When("게시글을 삭제하면") {
                postService.delete(deletePostCommand)
                
                Then("게시글이 삭제된다") {
                    verify(exactly = 1) { postRemover.delete(deletePostCommand) }
                }
            }
        }
        
        Given("문제 게시글 권한이 없을 때") {
            val userId = 1L
            val questionId = 1L
            val postId = 1L
            
            val registerPostCommand = RegisterPostCommand(
                questionId = questionId,
                writerId = userId,
                postContent = RegisterPostCommandPostContent(
                    title = "테스트 게시글",
                    content = "게시글 내용입니다",
                    files = emptyList()
                )
            )
            
            val postDetail = PostDetail(
                id = postId,
                questionId = questionId,
                title = "상세 게시글",
                content = "상세 내용입니다",
                files = listOf(
                    PostFile("file1.txt", "https://example.com/file1.txt"),
                    PostFile("file2.jpg", "https://example.com/file2.jpg")
                ),
                parentCategory = "수학",
                childCategory = "대수",
                questionTitle = "테스트 문제",
                writer = "게시글작성자",
                createdAt = LocalDateTime.now()
            )
            
            
            every { postPermissionValidator.validatePostPermission(any(), any()) } throws CoreException(Error.FORBIDDEN)
            every { postReader.getPostDetail(any(), any()) } returns postDetail
            
            When("게시글을 등록하면") {
                Then("예외가 발생한다.") {
                    shouldThrow<CoreException> {
                        postService.register(registerPostCommand)
                    }
                }
            }
            
            When("게시글 목록을 조회하면") {
                Then("예외가 발생한다.") {
                    shouldThrow<CoreException> {
                        postService.getPostPreviews(userId, questionId, PagingInformation.max)
                    }
                }
            }
            
            When("게시글 상세 정보를 조회하면") {
                Then("예외가 발생한다.") {
                    shouldThrow<CoreException> {
                        postService.getPostDetail(userId, postId)
                    }
                }
            }
        }
    }
}