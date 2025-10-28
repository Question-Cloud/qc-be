package com.eager.questioncloud.creator.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.creator.dto.CreatorInformationResponse
import com.eager.questioncloud.creator.dto.RegisterCreatorRequest
import com.eager.questioncloud.creator.dto.RegisterCreatorResponse
import com.eager.questioncloud.creator.service.CreatorInformationService
import com.eager.questioncloud.creator.service.RegisterCreatorService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/creator")
class CreatorController(
    private val creatorInformationService: CreatorInformationService,
    private val registerCreatorService: RegisterCreatorService,
) {
    @PostMapping("/register")
    fun registerCreator(
        userPrincipal: UserPrincipal, @RequestBody request: @Valid RegisterCreatorRequest
    ): RegisterCreatorResponse {
        val creator = registerCreatorService.register(userPrincipal.userId, request.mainSubject, request.introduction)
        return RegisterCreatorResponse(creator.id)
    }
    
    @GetMapping("/{creatorId}")
    fun getCreatorInformation(@PathVariable creatorId: Long): CreatorInformationResponse {
        val creatorInformation = creatorInformationService.getCreatorInformation(creatorId)
        return CreatorInformationResponse(creatorInformation)
    }
}
