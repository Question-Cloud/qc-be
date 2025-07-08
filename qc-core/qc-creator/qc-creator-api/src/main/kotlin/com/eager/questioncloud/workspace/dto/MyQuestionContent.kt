package com.eager.questioncloud.workspace.dto

class MyQuestionContent(
    val questionCategoryId: Long,
    val subject: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val fileUrl: String,
    val explanationUrl: String,
    val questionLevel: String,
    val price: Int
)