package com.eager.questioncloud.workspace.dto

class CreatorQuestionInformation(
    val id: Long,
    val creatorId: Long,
    val title: String,
    val subject: String,
    val parentCategory: String,
    val childCategory: String,
    val thumbnail: String,
    val questionLevel: String,
    val price: Int,
    val rate: Double,
)