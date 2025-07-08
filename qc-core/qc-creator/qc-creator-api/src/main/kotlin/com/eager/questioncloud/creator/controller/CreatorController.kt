package com.eager.questioncloud.creator.controller

import com.eager.questioncloud.creator.dto.CreatorInformationResponse
import com.eager.questioncloud.creator.service.CreatorInformationService
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
    fun getCreatorInformation(@PathVariable creatorId: Long): CreatorInformationResponse {
        val creatorInformation = creatorInformationService.getCreatorInformation(creatorId)
        return CreatorInformationResponse(creatorInformation)
    }
}
