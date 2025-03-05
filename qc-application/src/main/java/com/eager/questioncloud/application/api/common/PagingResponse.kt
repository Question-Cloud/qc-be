package com.eager.questioncloud.application.api.common

class PagingResponse<T>(
    val total: Int,
    val result: List<T>,
)
