package com.eager.questioncloud.application.api.creator.controller

import com.eager.questioncloud.application.api.creator.dto.CreatorInformationResponse
import com.eager.questioncloud.application.api.creator.service.CreatorInformationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/creator/info")
class CreatorController(
    private val creatorInformationService: CreatorInformationService
) {
    @GetMapping("/{creatorId}")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "크리에이터 정보 조회", summary = "크리에이터 정보 조회", tags = ["creator"], description = "크리에이터 정보 조회")
    fun getCreatorInformation(@PathVariable creatorId: Long): CreatorInformationResponse {
        val creatorInformation = creatorInformationService.getCreatorInformation(creatorId)
        return CreatorInformationResponse(creatorInformation)
    }
}
