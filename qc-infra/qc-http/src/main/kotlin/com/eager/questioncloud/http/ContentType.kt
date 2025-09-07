package com.eager.questioncloud.http

enum class ContentType(
    val contentType: String
) {
    JSON("application/json"), FORM("application/x-www-form-urlencoded")
}