package com.eager.questioncloud.workspace.dto

import com.eager.questioncloud.question.api.internal.RegisterQuestionCommand
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class RegisterQuestionRequest(
    @NotNull val questionCategoryId: Long,
    @NotNull val subject: String,
    @NotBlank val title: String,
    @NotBlank val description: String,
    @NotBlank val thumbnail: String,
    @NotBlank val fileUrl: String,
    @NotBlank val explanationUrl: String,
    @NotNull val questionLevel: String,
    @Min(value = 100) val price: Int,
) {
    fun toCommand(): RegisterQuestionCommand {
        return RegisterQuestionCommand(
            questionCategoryId,
            subject,
            title,
            description,
            thumbnail,
            fileUrl,
            explanationUrl,
            questionLevel,
            price
        )
    }
}