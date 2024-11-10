package com.eager.questioncloud.api.creator;

import com.eager.questioncloud.api.creator.Response.CreatorInformationResponse;
import com.eager.questioncloud.core.domain.creator.dto.CreatorDto.CreatorInformation;
import com.eager.questioncloud.core.domain.creator.service.CreatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/creator")
@RequiredArgsConstructor
public class CreatorController {
    private final CreatorService creatorService;

    @GetMapping("/{creatorId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "크리에이터 정보 조회", summary = "크리에이터 정보 조회", tags = {"creator"}, description = "크리에이터 정보 조회")
    public CreatorInformationResponse getCreatorInformation(@PathVariable Long creatorId) {
        CreatorInformation creatorInformation = creatorService.getCreatorInformation(creatorId);
        return new CreatorInformationResponse(creatorInformation);
    }
}
