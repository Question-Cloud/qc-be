package com.eager.questioncloud.application.exception

import com.eager.questioncloud.core.exception.CoreException
import org.springframework.http.ResponseEntity

class ErrorResponse(
    val status: Int,
    val message: String,
) {
    companion object {
        fun toResponse(e: CoreException): ResponseEntity<ErrorResponse> {
            return ResponseEntity
                .status(e.error.httpStatus)
                .body(ErrorResponse(e.error.httpStatus, e.error.message))
        }
    }
}