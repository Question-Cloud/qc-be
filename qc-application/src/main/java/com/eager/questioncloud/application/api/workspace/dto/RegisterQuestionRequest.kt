package com.eager.questioncloud.application.api.workspace.dto

import com.eager.questioncloud.core.domain.question.enums.QuestionLevel
import com.eager.questioncloud.core.domain.question.enums.QuestionType
import com.eager.questioncloud.core.domain.question.enums.Subject
import com.eager.questioncloud.core.domain.question.model.QuestionContent
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class RegisterQuestionRequest(
    @NotNull val questionCategoryId: Long,
    @NotNull val subject: Subject,
    @NotBlank val title: String,
    @NotBlank val description: String,
    @NotBlank val thumbnail: String,
    @NotBlank val fileUrl: String,
    @NotBlank val explanationUrl: String,
    @NotNull val questionLevel: QuestionLevel,
    @Min(value = 100) val price: Int,
) {
    fun toQuestionContent(): QuestionContent {
        return QuestionContent(
            questionCategoryId,
            subject,
            title,
            description,
            thumbnail,
            fileUrl,
            explanationUrl,
            QuestionType.SelfMade,
            questionLevel,
            price
        )
    }
}