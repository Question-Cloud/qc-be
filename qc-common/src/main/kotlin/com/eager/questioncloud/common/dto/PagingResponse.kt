package com.eager.questioncloud.common.dto

class PagingResponse<T>(
    val total: Int,
    val result: List<T>,
)
